package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

@Repository
class SubmitterDbRepository(private val databaseContext: DatabaseContext) {

    fun getSubmitterBySubmitterId(submitterId: Int): DbSubmitterDto? {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(SubmitterDao::class.java).getSubmitterBySubmitterId(submitterId)
        }
    }

    fun getSubmitterForSubmission(submissionId: Int): DbSubmitterDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(SubmitterDao::class.java)
                    .getSubmitterForSubmission(submissionId)
        }
    }

    fun insertSubmitter(type: String): DbSubmitterDto {
        return databaseContext.inTransaction { handle ->

            val submitterDao = handle.attach(SubmitterDao::class.java)

            return@inTransaction submitterDao
                    .insertSubmitter(type)
        }
    }
}