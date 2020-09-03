package pt.isel.ps.g06.httpserver

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.result.ResultIterator
import org.jdbi.v3.core.statement.StatementContext
import org.junit.Assert
import org.junit.jupiter.api.Test
import pt.isel.ps.g06.httpserver.util.asCachedSequence

class IterableCacheTest {

    @Test
    fun `cached Iterable should only process 3 times`() {
        var totalCalls = 0
        val cachedSequence = sequenceOf("a", "b", "c")
                .onEach {
                    totalCalls++
                }
                .asIterable()
                .asCachedSequence()
        Assert.assertEquals(0, totalCalls)
        cachedSequence.count()
        cachedSequence.count()
        cachedSequence.count()
        cachedSequence.count()
        Assert.assertEquals(3, totalCalls)
    }

    @Test
    fun `cached ResultIterable should close on fully cached`() {
        var totalCalls = 0
        var closedCalled = false
        val cachedSequence = ResultIterable.of(object: ResultIterator<Any> {
            val values = arrayOf("a", "b", "c")
            var idx = 0
            override fun hasNext(): Boolean {
                return idx < values.size
            }

            override fun next(): Any {
                if(!hasNext())
                    throw NoSuchElementException()
                totalCalls++
                return values[idx++]
            }

            override fun remove() = throw UnsupportedOperationException()

            override fun close() {
                closedCalled = true
            }

            override fun getContext(): StatementContext = throw UnsupportedOperationException()

        }).asCachedSequence()

        Assert.assertEquals(0, totalCalls)

        cachedSequence.count()
        Assert.assertTrue(closedCalled)

        cachedSequence.count()
        Assert.assertEquals(3, totalCalls)
    }

    @Test
    fun `cached ResultIterable should close on exception thrown`() {
        var closedCalled = false
        val cachedSequence = ResultIterable.of(object: ResultIterator<Any> {
            override fun hasNext(): Boolean = true

            override fun next() = throw NoSuchElementException()

            override fun remove() = throw UnsupportedOperationException()

            override fun close() {
                closedCalled = true
            }

            override fun getContext(): StatementContext = throw UnsupportedOperationException()

        }).asCachedSequence()

        try {
            cachedSequence.count()
        } catch (_: Exception) {

        }
        Assert.assertTrue(closedCalled)
    }
}