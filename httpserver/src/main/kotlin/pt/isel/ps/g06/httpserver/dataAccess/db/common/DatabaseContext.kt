package pt.isel.ps.g06.httpserver.dataAccess.db.common

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import pt.isel.ps.g06.httpserver.extensions.getOrPut

const val HANDLE_KEY = "pt.isel.ps.g06.httpserver.databaseContext"

@Component
class DatabaseContext(private val jdbi: Jdbi) {
    private val handle: Handle
        get() {
            return getOrPut(
                    key = HANDLE_KEY,
                    valueSupplier = { jdbi.open() },
                    callback = { it.close() }
            )
        }

    fun close() {
        handle.close()
        RequestContextHolder.resetRequestAttributes()
    }

    fun <T> inTransaction(func: (Handle) -> T): T {
        if (!handle.isInTransaction) {
            handle.begin()

            try {
                val result = func(handle)

                handle.commit()
                return result

            } catch (e: Exception) {
                handle.rollback()
                throw e
            }
        }

        return func(handle)
    }
}

