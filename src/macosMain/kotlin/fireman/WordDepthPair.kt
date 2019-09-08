package fireman

data class WordDepthPair(val word: String?, val depth : Int) : Comparable<WordDepthPair> {

    constructor() : this(null, 0)

    val length get() = word?.length ?: 0

    val isEmpty get() = (word == null || word.isEmpty()) && depth == 0

    override fun equals(other: Any?) : Boolean {
        return when (other) {
            null -> false
            is WordDepthPair -> this.depth == other.depth
            else -> false
        }
    }

    override operator fun compareTo(other: WordDepthPair) : Int {
        return when {
            this.depth < other.depth -> -1
            this.depth > other.depth -> 1
            else -> 0
        }
    }

    override fun hashCode(): Int {
        return depth
    }
}