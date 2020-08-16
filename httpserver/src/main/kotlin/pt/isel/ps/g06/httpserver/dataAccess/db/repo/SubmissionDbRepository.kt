package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ApiSubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import java.time.OffsetDateTime

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class SubmissionDbRepository(private val databaseContext: DatabaseContext) {
    fun getCreationDate(submissionId: Int): OffsetDateTime? {
        return databaseContext.inTransaction {
            it.attach(SubmissionDao::class.java).getById(submissionId)?.submission_date
        }
    }

    fun getSubmitterForSubmissionId(submissionId: Int): DbSubmitterDto? {
        return databaseContext.inTransaction {
            it.attach(SubmitterDao::class.java).getSubmitterForSubmission(submissionId)
        }
    }

    fun getApiSubmissionById(submissionId: Int): DbApiSubmissionDto? {
        return databaseContext.inTransaction {
            it.attach(ApiSubmissionDao::class.java).getBySubmissionId(submissionId)
        }
    }


    /**
     * Deletes given Submission from the database.
     * This deletion is cascading, so **any other tuples depending on given submission will be affected!**
     */
    fun deleteSubmission(submissionId: Int): DbSubmissionDto {
        return databaseContext.inTransaction {
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }
}