package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto

private val reportDaoClass = ReportDao::class.java

@Repository
class ReportDbRepository(private val databaseContext: DatabaseContext) {

    fun insert(submitterId: Int, submission_id: Int, report: String): DbReportDto {
        return databaseContext.inTransaction {
            it.attach(reportDaoClass).insert(submitterId, submission_id, report)
            //TODO Check if the submission exists and it is reportable
//            requireContract(submitterId, REPORTABLE, isolationLevel)
        }
    }
}