package comp2850.instr
import io.ktor.server.application.*
import io.ktor.util.*
import kotlin.system.*

val ReqIdKey = AttributeKey<String>("rid")

suspend inline fun <T> ApplicationCall.timed(taskCode: String, js: String, block: () -> T): T {
    val start = System.currentTimeMillis()
    return try {
        val res = block()
        val ms = System.currentTimeMillis() - start
        Logger.write(request.cookies["sid"] ?: "anon", attributes.getOrNull(ReqIdKey) ?: "na", taskCode, "success", "ok", ms, response.status()?.value ?: 200, js)
        res
    } catch (e: Exception) {
        val ms = System.currentTimeMillis() - start
        Logger.write(request.cookies["sid"] ?: "anon", attributes.getOrNull(ReqIdKey) ?: "na", taskCode, "server_error", e::class.simpleName ?: "error", ms, response.status()?.value ?: 500, js)
        throw e
    }
}

