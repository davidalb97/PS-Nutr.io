package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
//import pt.isel.ps.g06.httpserver.util.log

fun <R> inTransaction(jdbi: Jdbi, tl: TransactionIsolationLevel, func: (Handle) -> R): R? {
    try {
        jdbi.open().use { handle ->
            return handle.inTransaction<R, Exception>(tl, func)
        }
    } catch (e: Exception) {
        //log(e)
        return null
    }
}

fun inTransaction(jdbi: Jdbi, tl: TransactionIsolationLevel, func: (Handle) -> Boolean): Boolean {
    try {
        jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(tl, func)
        }
    } catch (e: Exception) {
        //log(e)
        return false
    }
}