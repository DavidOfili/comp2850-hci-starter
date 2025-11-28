package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.StringWriter
import PebbleEngineKey // <-- Import the key from Main.kt
//import io.ktor.server.application.ApplicationCall // <-- Import for ApplicationCall extension (isHtmx)

fun Route.taskRoutes() {
    val pebble = application.attributes[PebbleEngineKey]

    get("/tasks") {
        val model = mapOf(
            "title" to "Tasks",
            "tasks" to TaskRepository.all()
        )
        val template = pebble.getTemplate("tasks/index.peb") // <-- Correct path
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    
    post("/tasks") {
        val title = call.receiveParameters()["title"].orEmpty().trim()

		  // Validation
		  if (title.isBlank()) {
		      if (call.isHtmx()) {
		          val error = """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive">
		             Title is required. Please enter at least one character.
		          </div>"""
		          return@post call.respondText(error, ContentType.Text.Html, HttpStatusCode.BadRequest)
		      } else {
		          // No-JS path: redirect with error flag (handle in GET if needed)
		          return@post call.respondRedirect("/tasks?error=required")
		      }
		  }

        val task = TaskRepository.add(title)

		  if (call.isHtmx()) {
            // Return HTML fragment for new task
		      val fragment = """<li id="task-${task.id}">
		          <span>${task.title}</span>
		          <form action="/tasks/${task.id}/delete" method="post" style="display: inline;"
		               hx-post="/tasks/${task.id}/delete"
		               hx-target="#task-${task.id}"
		               hx-swap="outerHTML">
		           <button type="submit" aria-label="Delete task: ${task.title}">Delete</button>
		           </form>
		      </li>"""

		      val status = """<div id="status" hx-swap-oob="true">Task "${task.title}" added successfully.</div>"""

		      return@post call.respondText(fragment + status, ContentType.Text.Html, HttpStatusCode.Created)
		  }

        call.respondRedirect("/tasks") // No-JS fallback
    }

    
    post("/tasks/{id}/delete") {
    val id = call.parameters["id"]?.toIntOrNull()
    val removed = id?.let { TaskRepository.delete(it) } ?: false

    if (call.isHtmx()) {
        val message = if (removed) "Task deleted." else "Could not delete task."
        val status = """<div id="status" hx-swap-oob="true">$message</div>"""
        // Return empty content to trigger outerHTML swap (removes the <li>)
        return@post call.respondText(status, ContentType.Text.Html)
    }

    call.respondRedirect("/tasks")
}

	 // Return a single task in view mode (used by HTMX Cancel in inline edit)
    get("/tasks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest)

        val task = TaskRepository.get(id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val model = mapOf("task" to task)
        val template = pebble.getTemplate("tasks/_item.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)

        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    // Show the inline edit form for a single task
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest)

        val task = TaskRepository.get(id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        // For a minimal lab-correct version we support an "error" flag in the query string
        val errorParam = call.request.queryParameters["error"]
        val errorMessage = if (errorParam == "blank") {
            "Title is required. Please enter at least one character."
        } else null

        if (call.isHtmx()) {
            // HTMX path: return only the <li> edit fragment
            val model = mapOf(
                "task" to task,
                "error" to errorMessage
            )

            val template = pebble.getTemplate("tasks/_edit.peb")
            val writer = StringWriter()
            template.evaluate(writer, model)

            return@get call.respondText(writer.toString(), ContentType.Text.Html)
        } else {
            // No-JS path: full page render; index.peb decides which partial to include using editingId
            val model = mapOf(
                "title" to "Tasks",
                "tasks" to TaskRepository.all(),
                "editingId" to id,
                "errorMessage" to errorMessage
            )

            val template = pebble.getTemplate("tasks/index.peb")
            val writer = StringWriter()
            template.evaluate(writer, model)

            return@get call.respondText(writer.toString(), ContentType.Text.Html)
        }
    }

    // Handle saving changes to a task title (dual-path: HTMX and no-JS)
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest)

        val task = TaskRepository.get(id)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val params = call.receiveParameters()
        val newTitle = params["title"].orEmpty().trim()

        // Validation: title must not be blank
        if (newTitle.isBlank()) {
            if (call.isHtmx()) {
                // HTMX path: re-render the edit fragment with a simple error message
                val model = mapOf(
                    "task" to task,
                    "error" to "Title is required. Please enter at least one character."
                )

                val template = pebble.getTemplate("tasks/_edit.peb")
                val writer = StringWriter()
                template.evaluate(writer, model)

                return@post call.respondText(
                    writer.toString(),
                    ContentType.Text.Html,
                    HttpStatusCode.BadRequest
                )
            } else {
                // No-JS path: redirect back to edit page with ?error=blank
                return@post call.respondRedirect("/tasks/$id/edit?error=blank")
            }
        }

        // Update task title in the repository
        val updatedTask = TaskRepository.updateTitle(id, newTitle)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        if (call.isHtmx()) {
            // HTMX path: return the updated view-mode <li> fragment
            val model = mapOf("task" to updatedTask)
            val template = pebble.getTemplate("tasks/_item.peb")
            val writer = StringWriter()
            template.evaluate(writer, model)

            return@post call.respondText(writer.toString(), ContentType.Text.Html)
        }

        // No-JS path: standard PRG redirect back to the main list
        call.respondRedirect("/tasks")
    }

}
 

fun ApplicationCall.isHtmx(): Boolean =
    request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true
