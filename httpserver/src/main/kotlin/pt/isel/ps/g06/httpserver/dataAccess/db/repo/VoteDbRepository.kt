package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserVoteDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VotableDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserVoteDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val voteDaoClass = UserVoteDao::class.java

@Repository
class VoteDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getVotesById(submissionId: Int): DbVotesDto? {
        return jdbi.inTransaction<DbVotesDto, Exception>(isolationLevel) {
            it.attach(VotableDao::class.java).getById(submissionId)
        }
    }

    fun getUserVoteById(submissionId: Int, submitterId: Int): Boolean? {
        return jdbi.inTransaction<Boolean, Exception>(isolationLevel) {
            it.attach(voteDaoClass).getVoteByIds(submissionId, submitterId)
        }
    }

    fun insertOrUpdate(
            submitterId: Int,
            submissionId: Int,
            vote: Boolean
    ): DbUserVoteDto {
        return jdbi.inTransaction<DbUserVoteDto, Exception>(isolationLevel) {

            // Check if the submission exists and it is votable
            requireContract(submissionId, VOTABLE, isolationLevel)
            val voteDao = it.attach(voteDaoClass)
            val currentVotes = voteDao
                    .getVoteByIds(submissionId, submitterId)
            return@inTransaction if(currentVotes == null) {
                voteDao.insert(submitterId, submissionId, vote)
            } else voteDao.update(submitterId, submissionId, vote)
        }
    }

    fun delete(
            submitterId: Int,
            submission_id: Int
    ) {
        jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submission exists and it is votable
            requireContract(submission_id, VOTABLE, isolationLevel)

            // Check if this submitter already voted this submission
            requireVote(submission_id, submitterId, isolationLevel)

            // Remove the user's vote on that Submission
            it.attach(voteDaoClass)
                    .delete(submission_id, submitterId)
        }
    }
}