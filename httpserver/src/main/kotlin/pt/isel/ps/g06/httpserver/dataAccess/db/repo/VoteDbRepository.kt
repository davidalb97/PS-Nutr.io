package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VoteDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VotableDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes

private val serializable = TransactionIsolationLevel.SERIALIZABLE
private val voteDaoClass = VoteDao::class.java

@Repository
class VoteDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi, serializable) {

    fun getById(submissionId: Int): Votes? {
        return jdbi.inTransaction<Votes, Exception>(serializable) {
            it.attach(voteDaoClass).getVotes(submissionId)
        }
    }

    fun getSubmitterVoteById(submissionId: Int, submitterId: Int): Boolean? {
        return jdbi.inTransaction<Boolean, Exception>(serializable) {
            it.attach(voteDaoClass).getUserVoteById(submissionId, submitterId)
        }
    }

    fun insert(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ): VotableDto {
        return jdbi.inTransaction<VotableDto, Exception>(serializable) {

            // Check if the submission exists and it is votable
            requireContract(submission_id, VOTABLE)

            // Check if this submitter already voted this submission
            requireNoVote(submission_id, submitterId)

            // Insert a user's vote on that submission
            return@inTransaction it.attach(voteDaoClass)
                    .insert(submission_id, submitterId, vote)
        }
    }

    fun delete(
            submitterId: Int,
            submission_id: Int
    ) {
        jdbi.inTransaction<Unit, Exception>(serializable) {

            // Check if the submission exists and it is votable
            requireContract(submission_id, VOTABLE)

            // Check if this submitter already voted this submission
            requireVote(submission_id, submitterId)

            // Remove the user's vote on that Submission
            it.attach(voteDaoClass)
                    .delete(submission_id, submitterId)
        }
    }

    fun update(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ) {
        jdbi.inTransaction<Unit, Exception>(serializable) {

            // Check if the submission exists and it is votable
            requireContract(submission_id, VOTABLE)

            // Check if this submitter already voted this submission
            requireVote(submission_id, submitterId)

            it.attach(voteDaoClass)
                    .update(submission_id, submitterId, vote)
        }
    }
}