package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery

private const val votableTable = "Votable"
private const val voteSubmissionId = "submission_id"
private const val voteSubmitterId = "vote_submitter_id"
private const val vote = "vote"

interface VoteDao {

    @SqlQuery("INSERT INTO $votableTable($voteSubmissionId, $voteSubmitterId, $vote) " +
            "VALUES(:voteSubmissionId, :voteSubmitterId, :vote)")
    fun insertVote(@Bind voteSubmissionId: Int, voteSubmitterId: Int, vote: Boolean): Int

    @SqlQuery("DELETE FROM $votableTable WHERE $voteSubmissionId =" +
            " :submissionId, $voteSubmitterId = :voteSubmitterId")
    fun deleteVote(@Bind submissionId: Int, voteSubmitterId: Int): Int

    @SqlQuery("UPDATE SET $votableTable SET $vote = :vote WHERE $voteSubmissionId =" +
            " :submissionId, $voteSubmitterId = :voteSubmitterId")
    fun updateVote(@Bind submissionId: Int, voteSubmitterId: Int, vote: Boolean): Int
}