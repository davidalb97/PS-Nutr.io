package pt.isel.ps.g06.httpserver.util

import java.util.*
import kotlin.NoSuchElementException

/**
 * Memoize a Sequence.
 */
fun <T> Sequence<T>.memoized(): Sequence<T> {
    return asIterable().memoized().asSequence()
}

/**
 * Memoize a Iterable.
 */
fun <T> Iterable<T>.memoized(): Iterable<T> {
    val originalIterator: Iterator<T> = iterator()
    val memoizedValues = LinkedList<T>()

    return Iterable {
        object : Iterator<T> {
            private var idx = 0

            override fun hasNext(): Boolean {
                if (idx < memoizedValues.size) {
                    return true
                }
                return originalIterator.hasNext()
            }

            override fun next(): T {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                if (idx < memoizedValues.size) {
                    return memoizedValues[idx++]
                }
                return originalIterator.next().also { newOriginalValue ->
                    idx++
                    memoizedValues.addLast(newOriginalValue)
                }
            }
        }
    }
}