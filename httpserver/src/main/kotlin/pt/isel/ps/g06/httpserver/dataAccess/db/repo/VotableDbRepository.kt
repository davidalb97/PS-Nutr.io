package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VotableDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VotableDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain.VOTE
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

@Repository
class VotableDbRepository(private val jdbi: Jdbi) {

    private val serializable = TransactionIsolationLevel.SERIALIZABLE
    private val voteClass = VotableDao::class.java

    fun getById(submitterId: Int, submissionId: Int): Votes? {
        return inTransaction<Votes>(jdbi, serializable) {
            throw UnsupportedOperationException()
        }
    }

    fun insert(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ): VotableDto {
        return inTransaction(jdbi, serializable) {

            // Check if the submission exists
            validateSubmissionId(it, submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (hasVoted) {
                throw InvalidInputException(VOTE,
                        "The submitter id \"$submitterId\" cannot set vote on submission id " +
                                "\"$submission_id\" more than once."
                )
            }

            it.attach(VotableDao::class.java)
                    .insert(submission_id, submitterId, vote)
        }
    }

    fun removeVote(
            submitterId: Int,
            submission_id: Int
    ): VotableDto {
        return inTransaction(jdbi, serializable) {

            // Check if the submission exists
            validateSubmissionId(it, submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (!hasVoted) {
                throw InvalidInputException(VOTE,
                        "The submitter id \"$submitterId\" cannot delete vote submission id " +
                                "\"$submission_id\" without voting first."
                )
            }

            // Submit a report to that Submission
            it.attach(VotableDao::class.java)
                    .delete(submission_id, submitterId)
        }
    }

    fun updateVote(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ): VotableDto {
        return inTransaction(jdbi, serializable) {

            // Check if the submission exists
            validateSubmissionId(it, submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (!hasVoted) {
                // Submit a report to that Submission
                throw InvalidInputException(VOTE,
                        "The submitter id \"$submitterId\" cannot change vote on submission id " +
                                "\"$submission_id\" without voting first."
                )
            }

            it.attach(VotableDao::class.java)
                    .update(submission_id, submitterId, vote)
        }
    }
}