package pt.isel.ps.g06.httpserver

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.result.ResultIterator
import org.jdbi.v3.core.statement.StatementContext
import org.junit.Assert
import org.junit.jupiter.api.Test
import pt.isel.ps.g06.httpserver.util.asClosableSequence

class ClosableSequenceTests {

    @Test
    fun `ResultIterable should close on fully iterated on as a ClosableSequence`() {
        var totalCalls = 0
        var closedCalled = false
        val cachedSequence = ResultIterable.of(object : ResultIterator<Any> {
            val values = arrayOf("a", "b", "c")
            var idx = 0
            override fun hasNext(): Boolean {
                return idx < values.size
            }

            override fun next(): Any {
                if (!hasNext())
                    throw NoSuchElementException()
                totalCalls++
                return values[idx++]
            }

            override fun remove() = throw UnsupportedOperationException()

            override fun close() {
                closedCalled = true
            }

            override fun getContext(): StatementContext = throw UnsupportedOperationException()

        }).asClosableSequence()

        Assert.assertEquals(0, totalCalls)

        cachedSequence.count()
        Assert.assertTrue(closedCalled)
    }

    @Test
    fun `ResultIterable should close on exception thrown as a ClosableSequence`() {
        var closedCalled = false
        val cachedSequence = ResultIterable.of(
                object : ResultIterator<Any> {
                    override fun hasNext(): Boolean = true

                    override fun next() = throw NoSuchElementException()

                    override fun remove() = throw UnsupportedOperationException()

                    override fun close() {
                        closedCalled = true
                    }

                    override fun getContext(): StatementContext = throw UnsupportedOperationException()

                }
        ).asClosableSequence()

        try {
            cachedSequence.count()
        } catch (_: Exception) {

        }
        Assert.assertTrue(closedCalled)
    }
}