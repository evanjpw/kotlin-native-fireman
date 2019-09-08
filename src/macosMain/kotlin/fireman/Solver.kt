package fireman

class Solver {
    private val EMPTY_WORD_DEPTH_PAIR = WordDepthPair()
    private val tlBuckets : TwoLetterBuckets = TwoLetterBuckets()

    init {
        WordList.words.forEach { word -> tlBuckets.add(word) }

        tlBuckets.sortBuckets()
    }

    fun printBuckets() {
        for (bucket in tlBuckets) {
            println("-------")
            for (word in bucket) {
                println("     $word")
            }
        }
    }

    fun solve() : Pair<WordDepthPair, WordDepthPair> {
        var bestLength : WordDepthPair = EMPTY_WORD_DEPTH_PAIR
        var bestDepth : WordDepthPair = EMPTY_WORD_DEPTH_PAIR
        val words = WordList.reverseOrderedWords.takeWhile { it.length >= 3 }
        for (word in words) {
            val depth = tryWord(word)
            if (word.length < (3 * bestDepth.depth) && word.length < bestLength.length) {
                return Pair(bestDepth, bestLength)
                //continue
            }
            if (depth > bestDepth.depth) {
                bestDepth = WordDepthPair(word, depth)
            }
            if (word.length > bestLength.length) {
                bestLength = WordDepthPair(word, depth)
            }
        }
        return Pair(bestDepth, bestLength)
    }

    private fun tryWord(word: String, depth : Int = 0) : Int {
        if (word.isEmpty()) {
            return depth
        }
        var bestDepth = 0
        val bucket = tlBuckets.getByWord(word)
        bucket?.filter { it.length <= word.length && it.length >= 3}?.forEach {
              if (word.startsWith(it, ignoreCase = true)) {
                val nDepth = tryWord(word.substring(it.length), depth + 1)
                if (nDepth > bestDepth) {
                    bestDepth = nDepth
                }
            }
        }
        return bestDepth
    }
}