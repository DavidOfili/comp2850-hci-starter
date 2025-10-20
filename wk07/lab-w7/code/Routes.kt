// Routes.kt (sketch) — Week 7 Inline Edit
package comp2850

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

data class Task(val id: Int, val title: String)

fun ApplicationCall.isHtmx() = request.headers["HX-Request"] == "true"

// Replace with your actual renderer
fun render(tpl: String, model: Map<String, Any?>): String = "<!-- render $tpl with $model -->"

fun Route.inlineEditRoutes(repo: TaskRepo) {
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"]!!.toInt()
        val t = repo.get(id)
        val html = render("tasks/edit.peb", mapOf("t" to t, "error" to null))
        if (call.isHtmx()) call.respondText(html, ContentType.Text.Html) else call.respondText("Full page not implemented")
    }
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]!!.toInt()
        val params = call.receiveParameters()
        val title = (params["title"] ?: "").trim()
        val t = repo.get(id)
        if (title.isBlank()) {
            val html = render("tasks/edit.peb", mapOf("t" to t, "error" to "Title is required"))
            return@post call.respondText(html, ContentType.Text.Html)
        }
        val updated = repo.update(id, title)
        val viewHtml = render("tasks/view.peb", mapOf("t" to updated))
        val status = "<div id=\"status\" hx-swap-oob=\"true\">Saved “${title}”.</div>"
        call.respondText(viewHtml + status, ContentType.Text.Html)
    }
}

interface TaskRepo { fun get(id:Int): Task; fun update(id:Int, title:String): Task }

