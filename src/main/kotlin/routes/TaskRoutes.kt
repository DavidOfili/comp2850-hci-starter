package routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.http.*
import model.Task
import model.ValidationResult
import storage.TaskStore
import utils.SessionData
import isHtmxRequest
import renderTemplate

/**
 * Task management routes with HTMX progressive enhancement.
 *
 * **Dual-mode architecture**:
 * - Traditional: POST → Validate → Redirect (POST-Redirect-GET pattern)
 * - HTMX: POST → Validate → Return fragment + OOB status
 *
 * **Accessibility**:
 * - All features work without JavaScript
 * - ARIA live regions announce dynamic changes
 * - Keyboard navigation fully supported
 * - Validation errors linked to inputs
 */
fun Routing.configureTaskRoutes() {
    val store = TaskStore()

    /**
     * GET /tasks - List all tasks
     *
     * **Returns**:
     * - Full page with base layout, task list, add form
     * - No HTMX differentiation (always full page)
     */
    get("/tasks") {
        // Ensure session exists
        val session = call.sessions.get<SessionData>() ?: SessionData().also {
            call.sessions.set(it)
        }

        val tasks = store.getAll()

        val html = call.renderTemplate(
            "tasks/index.peb",
            mapOf(
                "tasks" to tasks.map { it.toPebbleContext() },
                "taskCount" to tasks.size
            )
        )

        call.respondText(html, ContentType.Text.Html)
    }

    /**
     * GET / - Root redirect
     * Redirects to /tasks for convenience
     */
    get("/") {
        call.respondRedirect("/tasks")
    }

    /**
     * POST /tasks - Add new task
     *
     * **Form parameters**:
     * - `title` (required): Task title
     *
     * **Dual-mode behavior**:
     * - **No-JS**: Validate → Redirect to /tasks (PRG pattern)
     * - **HTMX**: Validate → Return new task HTML fragment + OOB status message
     *
     * **Validation**:
     * - Title required, 3-100 characters
     * - Returns 422 Unprocessable Entity on validation error
     */
    post("/tasks") {
        val params = call.receiveParameters()
        val title = params["title"]?.trim() ?: ""

        // Validate title
        when (val result = Task.validate(title)) {
            is ValidationResult.Error -> {
                // Validation failed
                if (call.isHtmxRequest()) {
                    // HTMX mode: Return status message fragment with error
                    val statusHtml = """
                        <div id="status" hx-swap-oob="true" role="alert" class="error">
                            ${result.message}
                        </div>
                    """.trimIndent()
                    call.respondText(statusHtml, ContentType.Text.Html, HttpStatusCode.UnprocessableEntity)
                } else {
                    // No-JS mode: Redirect back with error (simplified - could use query params)
                    // For now, just redirect (error messaging would need session flash or query params)
                    call.respondRedirect("/tasks")
                }
                return@post
            }

            ValidationResult.Success -> {
                // Validation passed, create task
                val task = Task(title = title)
                store.add(task)

                if (call.isHtmxRequest()) {
                    // HTMX mode: Return new task HTML fragment + success status
                    val taskHtml = call.renderTemplate(
                        "tasks/_item.peb",
                        mapOf("task" to task.toPebbleContext())
                    )

                    val statusHtml = """
                        <div id="status" hx-swap-oob="true" role="status">
                            Task "${task.title}" added successfully.
                        </div>
                    """.trimIndent()

                    // Return both fragments (HTMX handles multiple elements)
                    call.respondText(taskHtml + "\n" + statusHtml, ContentType.Text.Html)
                } else {
                    // No-JS mode: Redirect to tasks page (PRG pattern)
                    call.respondRedirect("/tasks")
                }
            }
        }
    }

    /**
     * POST /tasks/{id}/toggle - Toggle task completion
     *
     * **Dual-mode behavior**:
     * - **No-JS**: Toggle → Redirect to /tasks
     * - **HTMX**: Toggle → Return updated task fragment + status
     *
     * **HTMX target**: Swaps entire task item (id="task-{id}")
     */
    post("/tasks/{id}/toggle") {
        val id = call.parameters["id"] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Missing task ID")
            return@post
        }

        val updated = store.toggleComplete(id)

        if (updated == null) {
            call.respond(HttpStatusCode.NotFound, "Task not found")
            return@post
        }

        if (call.isHtmxRequest()) {
            // HTMX mode: Return updated task item fragment
            val taskHtml = call.renderTemplate(
                "tasks/_item.peb",
                mapOf("task" to updated.toPebbleContext())
            )

            val statusText = if (updated.completed) "marked complete" else "marked incomplete"
            val statusHtml = """
                <div id="status" hx-swap-oob="true" role="status">
                    Task "${updated.title}" $statusText.
                </div>
            """.trimIndent()

            call.respondText(taskHtml + "\n" + statusHtml, ContentType.Text.Html)
        } else {
            // No-JS mode: Redirect back to tasks
            call.respondRedirect("/tasks")
        }
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     *
     * **Dual-mode behavior**:
     * - **No-JS**: Delete → Redirect to /tasks
     * - **HTMX**: Delete → Return empty (HTMX removes element)
     *
     * **HTMX swap**: `outerHTML` → replaces entire task item with empty string (removes from DOM)
     */
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Missing task ID")
            return@post
        }

        val task = store.getById(id)
        val deleted = store.delete(id)

        if (!deleted) {
            call.respond(HttpStatusCode.NotFound, "Task not found")
            return@post
        }

        if (call.isHtmxRequest()) {
            // HTMX mode: Return status message only (task item removed via hx-target swap)
            val statusHtml = """
                <div id="status" hx-swap-oob="true" role="status">
                    Task "${task?.title ?: "Unknown"}" deleted.
                </div>
            """.trimIndent()

            call.respondText(statusHtml, ContentType.Text.Html)
        } else {
            // No-JS mode: Redirect back to tasks
            call.respondRedirect("/tasks")
        }
    }

    /**
     * GET /tasks/search?q={query} - Search tasks
     *
     * **Query parameters**:
     * - `q`: Search query (case-insensitive substring match)
     *
     * **Dual-mode behavior**:
     * - **No-JS**: Returns full page with filtered results
     * - **HTMX**: Returns task list fragment only
     *
     * **HTMX target**: `#task-list` (replaces list, keeps form)
     */
    get("/tasks/search") {
        val query = call.request.queryParameters["q"]?.trim() ?: ""
        val tasks = store.search(query)

        if (call.isHtmxRequest()) {
            // HTMX mode: Return just the task list fragment
            val listHtml = call.renderTemplate(
                "tasks/_list.peb",
                mapOf(
                    "tasks" to tasks.map { it.toPebbleContext() },
                    "taskCount" to tasks.size,
                    "query" to query
                )
            )

            val statusHtml = if (query.isNotEmpty()) {
                """
                <div id="status" hx-swap-oob="true" role="status">
                    Found ${tasks.size} task(s) matching "$query".
                </div>
                """.trimIndent()
            } else {
                """
                <div id="status" hx-swap-oob="true" role="status"></div>
                """.trimIndent()
            }

            call.respondText(listHtml + "\n" + statusHtml, ContentType.Text.Html)
        } else {
            // No-JS mode: Return full page with search results
            val html = call.renderTemplate(
                "tasks/index.peb",
                mapOf(
                    "tasks" to tasks.map { it.toPebbleContext() },
                    "taskCount" to tasks.size,
                    "query" to query
                )
            )

            call.respondText(html, ContentType.Text.Html)
        }
    }
}
