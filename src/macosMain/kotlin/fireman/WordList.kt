package fireman

import kotlinx.cinterop.*
import platform.posix.*
import platform.osx.*

internal fun makeLengthComparator(reversed: Boolean = false) : Comparator<CharSequence> {
    val leftIsLess = if (reversed) 1 else -1
    val leftIsGreater = if (reversed) -1 else 1

    return Comparator { lhs, rhs ->
        val ll = lhs.length
        val rl = rhs.length
        when {
            ll < rl -> leftIsLess
            ll > rl -> leftIsGreater
            else -> 0
        }
    }
}

class CantOpenFileException(m: String) : Exception(m)

object WordList {
    private const val WORDS_LST = "./words.txt"
//    private val WORDS_LST_ENCODING = Charset.forName("UTF-8") fn

    private fun readWordList() : Array<String> {
        val resource = fopen(WORDS_LST, "r") ?: throw CantOpenFileException("File $WORDS_LST could not be opened.")
        val lines = ArrayList<String>()
        try {
            memScoped {
                val bufferLength = 64 * 1024
                val buffer = allocArray<ByteVar>(bufferLength)

                do {
                    val nextLine = fgets(buffer, bufferLength, resource)?.toKString()?.trim()
                    if (nextLine == null || nextLine.isEmpty()) return lines.toTypedArray()
                    lines.add(nextLine)
                } while (true)
            }
            } finally {
                fclose(resource) // fil
            }
        return emptyArray<String>()
    }

    val words by lazy {
        readWordList()
    }

    val reverseOrderedWords by lazy {
        words.sortedArrayWith(makeLengthComparator(reversed = true))
    }
}

typealias Bucket = List<String>

internal typealias MutableBucket = ArrayList<String>


internal fun MutableBucket.lengthOrder() = this.sortWith(fireman.makeLengthComparator())


abstract class BucketCollection<K : Comparable<K>> {
    protected val buckets : MutableMap<K, MutableBucket> = HashMap()

    abstract fun transformToKey(word : String) : K

    operator fun get(key : K) : Bucket? = buckets[key]

    fun getByWord(word : String) = get(transformToKey(word))

    operator fun contains(key: K) : Boolean = key in buckets

    operator fun plus(word : String) {
        val key = transformToKey(word)

        if (key !in buckets) {
            buckets[key] = ArrayList()
        }

        buckets[key]?.add(word)
    }

    fun add(word : String) = this + word

    fun sortBuckets(): Unit {
        for (( _, value: MutableBucket) in buckets)
        value.lengthOrder()
    }

    open operator fun iterator() : Iterator<Bucket> {
        return object : Iterator<Bucket> {
            val itr = buckets.iterator()

            override fun hasNext() = itr.hasNext()

            override fun next(): Bucket {
                val e = itr.next()
                return e.value
            }

        }
    }
}

class TwoLetterBuckets : BucketCollection<String>() {
    override fun transformToKey(word: String): String = if (word.length <= 2) {
        word
    } else {
        word.substring(0, 2)
    }
}

class LengthBuckets : BucketCollection<Int>() {
    override fun transformToKey(word: String): Int = word.length

    private val keyList by lazy {
        val bucketKeys: MutableSet<Int> = buckets.keys
        val keyList = bucketKeys.sortedBy { it }
        keyList.toSet().reversed()
    }

    override operator fun iterator(): Iterator<Bucket> {
        return object : Iterator<Bucket> {
            private var index = 0

            override fun hasNext() = index < keyList.size

            override fun next() = if (hasNext()) {
                val key = keyList[index++]
                buckets[key] ?: emptyList<String>()
            } else {
                throw NoSuchElementException()
            }
        }
    }
}
