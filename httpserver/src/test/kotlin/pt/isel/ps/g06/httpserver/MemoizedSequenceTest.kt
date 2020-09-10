package pt.isel.ps.g06.httpserver

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.result.ResultIterator
import org.jdbi.v3.core.statement.StatementContext
import org.junit.Assert
import org.junit.jupiter.api.Test
import pt.isel.ps.g06.httpserver.util.asClosableSequence
import pt.isel.ps.g06.httpserver.util.memoized

class MemoizedSequenceTest {

    @Test
    fun `Memoized Iterable Sequence should only process 3 times`() {
        var totalCalls = 0
        val cachedSequence = sequenceOf("a", "b", "c")
                .onEach {
                    totalCalls++
                }
                .memoized()

        Assert.assertEquals(0, totalCalls)
        cachedSequence.count()
        cachedSequence.count()
        cachedSequence.count()
        cachedSequence.count()
        Assert.assertEquals(3, totalCalls)
    }

    @Test
    fun `Memoized ResultIterable as ClosableSequence should only process 3 times and close`() {
        var totalCalls = 0
        var closedCalled = false
        val values = arrayOf("a", "b", "c")
        val cachedSequence = ResultIterable.of(object : ResultIterator<Any> {
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
                .memoized()

        Assert.assertEquals(0, totalCalls)

        //Iterated closure assert
        cachedSequence.count()
        Assert.assertTrue(closedCalled)
        Assert.assertEquals(values.size, totalCalls)

        //Memoization assert
        cachedSequence.count()
        Assert.assertEquals(values.size, totalCalls)
    }
}