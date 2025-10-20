// Kotlin CLI to analyse data/metrics.csv into analysis/analysis.csv and a quick summary.
// Usage: kotlinc Analyse.kt -include-runtime -d analyse.jar && java -jar analyse.jar
import java.io.File
import kotlin.math.abs
import kotlin.math.sqrt

data class Row(
    val task: String, val step: String, val ms: Long, val js: String
)

fun parse(file: File): List<Row> {
    if (!file.exists()) return emptyList()
    return file.readLines().drop(1).mapNotNull { line ->
        val parts = line.split(",")
        if (parts.size < 9) return@mapNotNull null
        val task = parts[3]; val step = parts[4]; val outcome = parts[5]
        val ms = parts[6].toLongOrNull() ?: 0L
        val js = parts[8]
        Row(task, step, ms, js)
    }
}

fun median(xs: List<Long>): Double {
    if (xs.isEmpty()) return Double.NaN
    val s = xs.sorted()
    val n = s.size
    return if (n % 2 == 1) s[n/2].toDouble() else (s[n/2 - 1] + s[n/2]).toDouble()/2.0
}

fun mad(xs: List<Long>, med: Double): Double {
    if (xs.isEmpty()) return Double.NaN
    val devs = xs.map { abs(it - med).toLong() }
    return median(devs).toDouble()
}

fun main() {
    val input = File("data/metrics.csv")
    val rows = parse(input)
    val outDir = File("analysis").apply { mkdirs() }
    val tasks = rows.map { it.task }.toSet().filter { it != "NA" }.sorted()
    val modes = listOf("on","off","all")

    val sb = StringBuilder()
    sb.appendLine("task_code,js_mode,n_success,median_ms,mad_ms,errors_validation,completion_rate,error_rate")

    for (t in tasks) {
        for (mode in modes) {
            val r = rows.filter { it.task == t && (mode == "all" || it.js == mode) }
            val succ = r.filter { it.step == "success" }
            val fail = r.filter { it.step == "fail" }
            val valErr = r.filter { it.step == "validation_error" }
            val med = median(succ.map { it.ms })
            val madv = mad(succ.map { it.ms }, med)
            val denomComp = (succ.size + fail.size).coerceAtLeast(1)
            val compRate = succ.size.toDouble() / denomComp
            val denomErr = (succ.size + valErr.size).coerceAtLeast(1)
            val errRate = valErr.size.toDouble() / denomErr
            sb.appendLine("$t,$mode,${succ.size},${"%.0f".format(med)},${"%.0f".format(madv)},${valErr.size},${"%.3f".format(compRate)},${"%.3f".format(errRate)}")
        }
    }

    File(outDir, "analysis.csv").writeText(sb.toString())
    File(outDir, "summary.md").appendText(
        """
        
        ## Auto-summary (appendix)
        Generated ${System.currentTimeMillis()} from ${input.path}
        Rows: ${rows.size}, Tasks: ${tasks.size}
        """.trimIndent()
    )
    println("Wrote analysis/analysis.csv")
}

