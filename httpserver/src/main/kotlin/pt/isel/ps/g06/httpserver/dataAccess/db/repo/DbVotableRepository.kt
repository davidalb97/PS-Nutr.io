package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VotableDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VotableDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes

class DbVotableRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val voteClass = VotableDao::class.java

    fun getVotes(submitterId: Int, submissionId: Int): Votes? {
        return inTransaction<Votes>(jdbi, serializable) {
            throw UnsupportedOperationException()
        }
    }

    fun addVote(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ): VotableDto? {
        return inTransaction(jdbi, serializable) {
            //validateSubmitterId(it, submitterId)

            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (submissionDto != null && !hasVoted) {
                // Submit a report to that Submission
                return@inTransaction it.attach(VotableDao::class.java)
                        .insert(submission_id, submitterId, vote)
            }

            null
        }
    }

    fun removeVote(
            submitterId: Int,
            submission_id: Int
    ): VotableDto? {
        return inTransaction(jdbi, serializable) {
            //validateSubmitterId(it, submitterId)

            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (submissionDto != null && !hasVoted) {
                // Submit a report to that Submission
                return@inTransaction it.attach(VotableDao::class.java)
                        .delete(submission_id, submitterId)
            }

            null
        }
    }

    fun updateVote(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ): VotableDto? {
        return inTransaction(jdbi, serializable) {
            //validateSubmitterId(it, submitterId)

            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (submissionDto != null && !hasVoted) {
                // Submit a report to that Submission
                return@inTransaction it.attach(VotableDao::class.java)
                        .update(submission_id, submitterId, vote)
            }

            null
        }
    }
}