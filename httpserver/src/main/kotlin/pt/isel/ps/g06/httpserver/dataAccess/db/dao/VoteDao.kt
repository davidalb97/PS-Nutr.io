package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VotableDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes

private const val table = "Vote"
private const val submissionId = "submission_id"
private const val voterSubmitterId = "vote_submitter_id"
private const val vote = "vote"

interface VoteDao {

    @SqlQuery("SELECT" +
            " Count(case when $vote = true then 1 end)," +
            " Count(case when $vote = false then 1 end)" +
            " FROM $table" +
            " WHERE $submissionId = :submissionId")
    fun getVotes(@Bind submissionId: Int): Votes?

    @SqlQuery("SELECT $vote" +
            " FROM $table" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voteSubmitterId"
    )
    fun getUserVoteById(@Bind submissionId: Int, voteSubmitterId: Int): Boolean?

    @SqlQuery("INSERT INTO $table($submissionId, $voterSubmitterId, $vote)" +
            " VALUES(:voteSubmissionId, :voteSubmitterId, :vote) RETURNING *")
    fun insert(@Bind voteSubmissionId: Int, voterSubmitterId: Int, vote: Boolean): VotableDto

    @SqlUpdate("UPDATE $table" +
            " SET $vote = :vote" +
            " WHERE $submissionId =" +
            " :submissionId, $voterSubmitterId = :voteSubmitterId RETURNING *"
    )
    fun update(@Bind submissionId: Int, voteSubmitterId: Int, vote: Boolean): VotableDto

    @SqlUpdate("DELETE FROM $table" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voteSubmitterId RETURNING *")
    fun delete(@Bind submissionId: Int, voteSubmitterId: Int): VotableDto
}