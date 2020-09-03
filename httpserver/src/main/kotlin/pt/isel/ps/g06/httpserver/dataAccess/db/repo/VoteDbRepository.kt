package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.NonVotableSubmissionException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserVoteDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VotableDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.model.VoteState

private val voteDaoClass = UserVoteDao::class.java

@Repository
class VoteDbRepository(private val databaseContext: DatabaseContext, private val submissionDbRepository: SubmissionDbRepository) {
    //It is not required to run exhaustive when for equal states.
    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun setVote(voterId: Int, submissionId: Int, newVote: VoteState) {
        return databaseContext.inTransaction { handle ->
            // Check if the submission exists and it is votable
            if (!submissionDbRepository.hasContract(submissionId, VOTABLE)) {
                throw NonVotableSubmissionException()
            }

            // Check if this submitter already voted this submission
            val oldVote = handle
                    .attach(UserVoteDao::class.java)
                    .getUserVoteForSubmission(submissionId, voterId)
                    ?.let { if (it.vote) VoteState.POSITIVE else VoteState.NEGATIVE }
                    ?: VoteState.NOT_VOTED

            if (oldVote == newVote) {
                return@inTransaction
            }

            val voteDao = handle.attach(voteDaoClass)
            var positiveOffset = 0
            var negativeOffset = 0

            when (oldVote) {
                VoteState.NOT_VOTED -> {
                    when (newVote) {
                        VoteState.NEGATIVE -> negativeOffset++
                        VoteState.POSITIVE -> positiveOffset++
                    }
                    voteDao.insert(submissionId, voterId, newVote == VoteState.POSITIVE)
                }
                VoteState.NEGATIVE -> when (newVote) {
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
                VoteState.POSITIVE -> when (newVote) {
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
            handle.attach(VotableDao::class.java).incrementVotes(
                    submissionId = submissionId,
                    positiveOffset = positiveOffset,
                    negativeOffset = negativeOffset
            )
        }
    }

    fun getVotes(submissionId: Int): DbVotesDto {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(VotableDao::class.java).getById(submissionId)
        } ?: DbVotesDto(0, 0)
    }

    fun getUserVote(
            submissionId: Int,
            userId: Int
    ): VoteState {
        return databaseContext.inTransaction { handle ->
            val dto = handle
                    .attach(UserVoteDao::class.java)
                    .getUserVoteForSubmission(submissionId, userId)
                    ?: return@inTransaction VoteState.NOT_VOTED

            return@inTransaction if (dto.vote) VoteState.POSITIVE else VoteState.NEGATIVE
        }
    }
}