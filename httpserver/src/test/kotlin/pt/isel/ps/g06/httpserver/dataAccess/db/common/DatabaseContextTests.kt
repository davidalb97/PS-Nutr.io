package pt.isel.ps.g06.httpserver.dataAccess.db.common

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@SpringBootTest
class DatabaseContextTests {
    @Autowired
    lateinit var jdbi: Jdbi
    lateinit var databaseContext: DatabaseContextTestingExtension

    @BeforeEach
    fun setupContext() {
        databaseContext = DatabaseContextTestingExtension(jdbi)
    }

    @Test
    fun `transactional work should open the handle`() {
        //Simulate some transactional work to open the handle...
        databaseContext.inTransaction { }

        Assert.assertEquals(1, databaseContext.timesOpened)
    }

    @Test
    fun `jdbi handle open should only be lazily open only when requested`() {
        Assert.assertEquals(0, databaseContext.timesOpened)

        //Simulate some transactional work to open the handle...
        databaseContext.inTransaction { }

        Assert.assertEquals(1, databaseContext.timesOpened)
    }

    @Test
    fun `jdbi handle open should be cached on subsequent transactional calls`() {
        Assert.assertEquals(0, databaseContext.timesOpened)

        //Simulate some transactional work to open the handle...
        databaseContext.inTransaction { }

        Assert.assertEquals(1, databaseContext.timesOpened)

        //Simulate another transaction work
        databaseContext.inTransaction { }
        Assert.assertEquals(1, databaseContext.timesOpened)
    }

    @Test
    fun `different threads should open different database handles`() {
        val first = CompletableFuture.runAsync { databaseContext.inTransaction { } }
        val second = CompletableFuture.runAsync { databaseContext.inTransaction { } }

        first.get()
        second.get()

        Assert.assertEquals(2, databaseContext.timesOpened)
    }
}

@Component
class DatabaseContextTestingExtension(jdbi: Jdbi) : DatabaseContext(jdbi) {
    var timesOpened = 0
    val auxHandle: ThreadLocal<Handle> = ThreadLocal.withInitial { timesOpened++; super.localHandle.get() }

    override val localHandle: ThreadLocal<Handle> get() = auxHandle
}