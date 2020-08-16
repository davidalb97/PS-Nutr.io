package pt.isel.ps.g06.httpserver.dataAccess.db.common

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

@Component
class DatabaseContext(jdbi: Jdbi) {
    //TODO Unit test to see if handle is lazily open
    //TODO Unit test to verify if context is a singleton
    private val localHandle: ThreadLocal<Handle> = ThreadLocal.withInitial { jdbi.open() }
    private val isOpen: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }

    private val handle: Handle
        get() {
            println("Get handle with Thread ID {${Thread.currentThread().id}}")
            isOpen.set(true)
            return localHandle.get()
        }

    fun close() {
        handle.close()
        localHandle.remove()
        isOpen.remove()
    }

    fun isHandleOpen(): Boolean = isOpen.get()

    fun <T> inTransaction(func: (Handle) -> T): T {
        if (!handle.isInTransaction) {
            handle.begin()

            try {
                val result = func(handle)

                handle.commit()
                return result

            } catch (e: Exception) {
                handle.rollback()
            }
        }

        return func(handle)
    }
}

