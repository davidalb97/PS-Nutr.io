package pt.isel.ps.g06.httpserver.util

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.result.ResultIterator
import java.io.Closeable
import java.util.*
import kotlin.NoSuchElementException

fun interface ClosableSequence<T> : Sequence<T>, Closeable {
    override fun close() {}
}

fun <T> emptyClosableSequence() = ClosableSequence { emptySequence<T>().iterator() }

fun <T> closableSequenceOf(vararg items: T) = ClosableSequence { items.iterator() }

fun <T> ResultIterable<T>.asClosableSequence(): ClosableSequence<T> {
    val originalIterator: ResultIterator<T> = iterator()
    var closed = false

    return object : ClosableSequence<T> {
        override fun iterator() = object : Iterator<T> {
            override fun hasNext(): Boolean {
                return try {
                    originalIterator.hasNext().also { hasNext ->
                        if (!hasNext) {
                            close()
                        }
                    }
                } catch (e: Exception) {
                    close()
                    false
                }
            }

            override fun next(): T {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                return try {
                    originalIterator.next()
                } catch (e: Exception) {
                    close()
                    throw e
                }
            }
        }

        override fun close() {
            if (!closed) {
                closed = true
                originalIterator.close()
            }
        }
    }
}


fun <T> Sequence<T>.memoized(): Sequence<T> {
    val originalIterator: Iterator<T> = iterator()
    val memoizedValues = LinkedList<T>()
    return Sequence {
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