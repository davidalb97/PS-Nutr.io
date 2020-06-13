package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserVoteDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.model.VoteState
import java.time.OffsetDateTime

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class SubmissionDbRepository(jdbi: Jdbi): BaseDbRepo(jdbi) {

    fun getCreationDate(submissionId: Int): OffsetDateTime? {
        return jdbi.inTransaction<OffsetDateTime, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(SubmissionDao::class.java).getById(submissionId)
                    ?.submission_date
        }
    }

    fun getSubmitterById(submissionId: Int): DbSubmitterDto? {
        return jdbi.inTransaction<DbSubmitterDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(SubmitterDao::class.java)
                    .getAllBySubmissionId(submissionId).firstOrNull()
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
            val vote = handle.attach(UserVoteDao::class.java).getVoteByIds(submissionId, userId)
                    ?: return@inTransaction VoteState.NOT_VOTED
            return@inTransaction if(vote) VoteState.POSITIVE else VoteState.NEGATIVE
        }
    }

    /**
     * Removes submission, submitter association, contracts & it's tables
     */
    protected fun removeSubmission(
            submissionId: Int,
            submitterId: Int,
            type: SubmissionType,
            contracts: Collection<SubmissionContractType>,
            isolationLevel: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE
    ) {
        return jdbi.inTransaction<Unit, InvalidInputException>(isolationLevel) {

            // Check if the submission is of the specified type
            requireSubmission(submissionId, type, isolationLevel)

            if (contracts.isNotEmpty()) {
                // Delete all submission contracts
                it.attach(SubmissionContractDao::class.java).deleteAllBySubmissionId(submissionId)
            }

            if (contracts.contains(SubmissionContractType.REPORTABLE)) {
                // Delete all user reports
                it.attach(ReportDao::class.java).deleteAllBySubmissionId(submissionId)
            }

            if (contracts.contains(SubmissionContractType.VOTABLE)) {
                // Delete all user votes
                it.attach(UserVoteDao::class.java).deleteAllById(submissionId)
                // Delete vote counter
                it.attach(VotableDao::class.java).deleteById(submissionId)
            }

            if (contracts.contains(SubmissionContractType.API) && isFromApi(submissionId)) {
                // Delete api submission relation
                it.attach(ApiSubmissionDao::class.java).deleteById(submissionId)
            }

            if (contracts.contains(SubmissionContractType.FAVORABLE)) {
                // Delete all favorites
                it.attach(FavoriteDao::class.java).deleteAllBySubmissionId(submissionId)
            }

            // Delete submission - submitter association
            it.attach(SubmissionSubmitterDao::class.java).deleteAllBySubmissionId(submissionId)

            // Delete submission
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }
}