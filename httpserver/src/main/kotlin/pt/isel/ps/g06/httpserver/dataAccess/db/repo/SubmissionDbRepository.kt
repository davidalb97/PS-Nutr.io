package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.model.VoteState
import java.time.OffsetDateTime

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class SubmissionDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getCreationDate(submissionId: Int): OffsetDateTime? {
        return jdbi.inTransaction<OffsetDateTime, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(SubmissionDao::class.java).getById(submissionId)
                    ?.submission_date
        }
    }

    fun getSubmitterForSubmissionId(submissionId: Int): DbSubmitterDto? {
        return jdbi.inTransaction<DbSubmitterDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(SubmitterDao::class.java)
                    .getSubmitterForSubmission(submissionId)
        }
    }

    fun getApiSubmissionById(submissionId: Int): DbApiSubmissionDto? {
        return jdbi.inTransaction<DbApiSubmissionDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(ApiSubmissionDao::class.java).getBySubmissionId(submissionId)
        }
    }

    fun getVotes(submissionId: Int): DbVotesDto {
        return jdbi.inTransaction<DbVotesDto?, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(VotableDao::class.java).getById(submissionId)
        } ?: DbVotesDto(0, 0)
    }

    fun getUserVote(
            submissionId: Int,
            userId: Int
    ): VoteState {
        return jdbi.inTransaction<VoteState, Exception>(isolationLevel) { handle ->
            val dto = handle
                    .attach(UserVoteDao::class.java)
                    .getUserVoteForSubmission(submissionId, userId)
                    ?: return@inTransaction VoteState.NOT_VOTED

            return@inTransaction if (dto.vote) VoteState.POSITIVE else VoteState.NEGATIVE
        }
    }

    /**
     * Deletes given Submission from the database.
     * This deletion is cascading, so **any other tuples depending on given submission will be affected!**
     */
    fun deleteSubmission(submissionId: Int): DbSubmissionDto {
        return jdbi.inTransaction<DbSubmissionDto, Exception>(isolationLevel) {
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }
}