package pt.isel.ps.g06.httpserver.dataAccess.common

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

class TransactionHolder(private val jdbi: Jdbi) {

    fun <R> inTransaction(func: (Handle) -> R) {
        jdbi.inTransaction<R, Exception>(func)
    }
}