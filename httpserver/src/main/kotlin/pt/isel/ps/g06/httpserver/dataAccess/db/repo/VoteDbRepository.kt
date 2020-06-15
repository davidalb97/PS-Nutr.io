package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.clientError.NotYetVotedException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserVoteDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VotableDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserVoteDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val voteDaoClass = UserVoteDao::class.java

@Repository
class VoteDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {
    fun insertOrUpdate(voterId: Int, submissionId: Int, vote: Boolean): DbUserVoteDto {
        return jdbi.inTransaction<DbUserVoteDto, Exception>(isolationLevel) {
            // Check if the submission exists and it is votable
            requireContract(submissionId, VOTABLE, isolationLevel)

            val voteDao = it.attach(voteDaoClass)
            val userVoteForSubmission = voteDao.getUserVoteForSubmission(submissionId, voterId)

            val userVote: DbUserVoteDto
            val positive: Int
            val negative: Int

            if (userVoteForSubmission == null) {
                //User has not voted yet
                userVote = voteDao.insert(submissionId, voterId, vote)
                positive = if (vote) 1 else 0
                negative = if (vote) 0 else 1
            } else {
                if (userVoteForSubmission.vote == vote) {
                    //If vote call is the same has vote intent, no point in updating
                    return@inTransaction userVoteForSubmission
                } else {
                    userVote = voteDao.update(submissionId, voterId, vote)
                    positive = if (vote) 1 else -1
                    negative = if (vote) -1 else 1
                }

            }

            //Update vote number count
            it.attach(VotableDao::class.java).incrementVotes(submissionId, positive, negative)
            return@inTransaction userVote
        }
    }

    fun delete(submitterId: Int, submissionId: Int) {
        jdbi.inTransaction<Unit, Exception>(isolationLevel) {
            // Check if the submission exists and it is votable
            requireContract(submissionId, VOTABLE, isolationLevel)

            // Check if this submitter already voted this submission
            val userVote = it
                    .attach(UserVoteDao::class.java)
                    .getUserVoteForSubmission(submissionId, submitterId)
                    ?: throw NotYetVotedException()

            // Remove the user's vote on that Submission
            it.attach(voteDaoClass).delete(submissionId, submitterId)

            //Update vote number count
            it.attach(VotableDao::class.java).incrementVotes(
                    submissionId = submissionId,
                    positiveOffset = if (userVote.vote) -1 else 0,
                    negativeOffset = if (userVote.vote) 0 else -1
            )
        }
    }
}