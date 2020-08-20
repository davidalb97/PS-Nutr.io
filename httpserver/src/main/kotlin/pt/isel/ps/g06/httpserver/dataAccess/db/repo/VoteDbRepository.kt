package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.clientError.NonVotableSubmissionException
import pt.isel.ps.g06.httpserver.common.exception.clientError.NotYetVotedException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserVoteDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VotableDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserVoteDto
import pt.isel.ps.g06.httpserver.model.VoteState

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val voteDaoClass = UserVoteDao::class.java

@Repository
class VoteDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    //It is not required to run exhaustive when for equal states.
    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun setVote(voterId: Int, submissionId: Int, newVote: VoteState) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {
            // Check if the submission exists and it is votable
            if (!requireContract(submissionId, VOTABLE, isolationLevel)) {
                throw NonVotableSubmissionException()
            }

            // Check if this submitter already voted this submission
            val oldVote = it
                    .attach(UserVoteDao::class.java)
                    .getUserVoteForSubmission(submissionId, voterId)
                    ?.let { if(it.vote) VoteState.POSITIVE else VoteState.NEGATIVE }
                    ?: VoteState.NOT_VOTED

            val voteDao = it.attach(voteDaoClass)

            if(oldVote == newVote) {
                return@inTransaction
            }
            var positiveOffset = 0
            var negativeOffset = 0

            when(oldVote) {
                VoteState.NOT_VOTED -> {
                    when(newVote) {
                        VoteState.NEGATIVE -> negativeOffset++
                        VoteState.POSITIVE -> positiveOffset++
                    }
                    voteDao.insert(submissionId, voterId, newVote == VoteState.POSITIVE)
                }
                VoteState.NEGATIVE -> when(newVote) {
                    VoteState.NOT_VOTED -> {
                        negativeOffset--
                        voteDao.delete(submissionId, voterId)
                    }
                    VoteState.POSITIVE -> {
                        positiveOffset++
                        negativeOffset--
                        voteDao.insert(submissionId, voterId, true)
                    }
                }
                VoteState.POSITIVE -> when(newVote) {
                    VoteState.NOT_VOTED -> {
                        positiveOffset--
                        voteDao.delete(submissionId, voterId)
                    }
                    VoteState.NEGATIVE -> {
                        positiveOffset--
                        negativeOffset++
                        voteDao.insert(submissionId, voterId, false)
                    }
                }
            }

            //Update vote number count
            it.attach(VotableDao::class.java).incrementVotes(
                    submissionId = submissionId,
                    positiveOffset = positiveOffset,
                    negativeOffset = negativeOffset
            )
        }
    }
}