package pt.isel.ps.g06.httpserver.util

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.result.ResultIterator
import java.util.*
import kotlin.NoSuchElementException

fun <T> ResultIterable<T>.asCachedSequence(): Sequence<T> {
    val originalIterator: ResultIterator<T> = iterator()
    val cachedValues = LinkedList<T>()
    var closed = false
    return Sequence {
        object : Iterator<T>, AutoCloseable {
            private var idx = 0
            override fun close() {
                if (!closed) {
                    closed = true
                    originalIterator.close()
                }
            }

            override fun hasNext(): Boolean {
                if (idx < cachedValues.size) {
                    return true
                }
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
                if (idx < cachedValues.size) {
                    return cachedValues[idx++]
                }
                return try {
                    originalIterator.next().also { newOriginalValue ->
                        idx++
                        cachedValues.addLast(newOriginalValue)
                    }
                } catch (e: Exception) {
                    close()
                    throw e
                }
            }
        }
    }
}

fun <T> Iterable<T>.asCachedSequence(): Sequence<T> {
    val originalIterator: Iterator<T> = iterator()
    val cachedValues = LinkedList<T>()
    return Sequence {
        object : Iterator<T> {
            private var idx = 0

            override fun hasNext(): Boolean {
                if (idx < cachedValues.size) {
                    return true
                }
                return originalIterator.hasNext()
            }

            override fun next(): T {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                if (idx < cachedValues.size) {
                    return cachedValues[idx++]
                }
                return originalIterator.next().also { newOriginalValue ->
                    idx++
                    cachedValues.addLast(newOriginalValue)
                }
            }
        }
    }
}