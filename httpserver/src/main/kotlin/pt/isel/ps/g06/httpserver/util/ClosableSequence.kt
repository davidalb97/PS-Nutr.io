package pt.isel.ps.g06.httpserver.util

import org.jdbi.v3.core.result.ResultIterable
import java.io.Closeable
import java.util.*
import kotlin.NoSuchElementException

fun interface ClosableSequence<T> : Sequence<T>, Closeable {
    override fun close() {}
}


fun <T> emptyClosableSequence() = ClosableSequence { emptySequence<T>().iterator() }

/**
 * Creates a [ClosableSequence] from a list of values.
 */
fun <T> closableSequenceOf(vararg items: T, onClose: (Iterator<T>) -> Unit = {}) =
        closableSequence({ items.iterator() }, onClose)

/**
 * Creates a [ClosableSequence] from an [Iterator] supplier and closes calling [onClose].
 */
fun <T, I> closableSequence(iteratorSupplier: () -> I, onClose: (I) -> Unit)
        where I : Iterator<T> = object : ClosableSequence<T> {
    val originalIterator: I = iteratorSupplier()
    var closed = false

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
            onClose(originalIterator)
        }
    }
}

/**
 * Creates a [ClosableSequence] from a supplier of [Iterator]s that implement Closeable.
 */
fun <T, P> closableSequence(iteratorSupplier: () -> P) where P : Iterator<T>, P : Closeable =
        closableSequence(iteratorSupplier) { it.close() }

/**
 * Creates a [ClosableSequence] from an [ResultIterable], using it's [Iterator] to close.
 */
fun <T> ResultIterable<T>.asClosableSequence(): ClosableSequence<T> = closableSequence { iterator() }

/**
 * Creates a [ClosableSequence] from an [Iterable] that implements Closeable.
 */
fun <T, P> P.asClosableSequence() where P : Iterable<T>, P : Closeable = closableSequence({ iterator() }) { close() }

/**
 * Creates a [ClosableSequence] from an [Iterable] and closes using [onClose].
 */
fun <T> Iterable<T>.asClosableSequence(onClose: (Iterator<T>) -> Unit) = closableSequence({ iterator() }) {
    onClose(it)
}