package utils

/**
 * Simple timing utility for measuring request duration.
 *
 * Usage:
 *   val timing = Timing.start()
 *   // ... do work ...
 *   val durationMs = timing.elapsed()
 *   Logger.write(..., ms = durationMs, ...)
 *
 * Referenced in: Week 9 Lab 1 (Activity 3)
 */
class Timing private constructor(private val startNano: Long) {
    companion object {
        fun start(): Timing = Timing(System.nanoTime())
    }

    /**
     * Returns elapsed time in milliseconds since start().
     */
    fun elapsed(): Long = (System.nanoTime() - startNano) / 1_000_000
}
