package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel

/**
 * Runs func with a jdbi transaction handle and rolls back after completion.
 */
fun <R> Jdbi.inRollbackTransaction(isolation: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE, func: (Handle) -> R) {
    inTransaction<R, Exception>(isolation) { handle ->
        func(handle).also { handle.rollback() }
    }
}

fun nextSerialValue(jdbi: Jdbi, tableName: String, columnName: String): Int {
    return jdbi.open().inTransaction<Int, Exception>(TransactionIsolationLevel.SERIALIZABLE) {
        val nextVal = it.createQuery("SELECT * FROM nextval(pg_get_serial_sequence('$tableName', '$columnName'))")
                .map { rs, _ -> rs.getInt("nextval") }
                .list()
                .first()
        it.createUpdate("SELECT setval(pg_get_serial_sequence('$tableName', '$columnName'), ${nextVal - 1})")
                .execute()
        return@inTransaction nextVal
    }
}