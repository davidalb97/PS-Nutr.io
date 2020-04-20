package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao

class DbReportRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val reportDao = ReportDao::class.java

    fun insert(
            submitterId: Int,
            submissionId: Int,
            description: String
    ): Boolean {
        return inTransaction(jdbi, serializable) {
            val reportDao = it.attach(reportDao)
            reportDao.insert(submitterId, submissionId, description)

            true
        }
    }
}