package fireman

import platform.posix.*

const val DEBUG = false

fun main(args: Array<String>) {
    println("Working")

    try {
        val solver = Solver()

         if (DEBUG) solver.printBuckets()

        val (bestDepth, _) = solver.solve()  //bestLength

        if (!bestDepth.isEmpty) {
            println("Found word with greatest number of sub-words: \"${bestDepth.word}\", with a count of ${bestDepth.depth} sub-words.")
        } else {
            println("No word found that meets the criteria.")
        }
    } catch (e: CantOpenFileException) {
        perror(e.message)
    } finally {
        println("Done")
    }
}
