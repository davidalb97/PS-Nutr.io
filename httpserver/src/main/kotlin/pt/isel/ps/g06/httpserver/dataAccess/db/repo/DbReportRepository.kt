package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao

class DbReportRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val reportDao = ReportDao::class.java

    fun submitReport(
            submitterId: Int,
            submissionId: Int,
            description: String
    ): Boolean {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(serializable) {
                val reportDao = it.attach(ReportDao::class.java)
                reportDao.insert(submitterId, submissionId, description)
            }
        }
    }
}