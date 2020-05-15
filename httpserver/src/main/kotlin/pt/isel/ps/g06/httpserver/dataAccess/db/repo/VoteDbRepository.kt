package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VoteDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VoteDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val voteDaoClass = VoteDao::class.java

@Repository
class VoteDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getById(submissionId: Int): Votes? {
        return jdbi.inTransaction<Votes, Exception>(isolationLevel) {
            it.attach(voteDaoClass).getVotes(submissionId)
        }
    }

    fun getSubmitterVoteById(submissionId: Int, submitterId: Int): Boolean? {
        return jdbi.inTransaction<Boolean, Exception>(isolationLevel) {
            it.attach(voteDaoClass).getUserVoteById(submissionId, submitterId)
        }
    }

    fun insertOrUpdate(
            submitterId: Int,
            submissionId: Int,
            vote: Boolean
    ): VoteDto {
        return jdbi.inTransaction<VoteDto, Exception>(isolationLevel) {

            // Check if the submission exists and it is votable
            requireContract(submissionId, VOTABLE, isolationLevel)
            val voteDao = it.attach(voteDaoClass)
            val currentVotes = voteDao
                    .getUserVoteById(submissionId, submitterId)
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