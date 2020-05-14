package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VoteDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes

interface VoteDao {

    companion object {
        const val table = "Vote"
        const val submissionId = "submission_id"
        const val voterSubmitterId = "vote_submitter_id"
        const val vote = "vote"
    }

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
    fun insert(@Bind voteSubmissionId: Int, voterSubmitterId: Int, vote: Boolean): VoteDto

    @SqlQuery("UPDATE $table" +
            " SET $vote = :vote" +
            " WHERE $submissionId =" +
            " :submissionId, $voterSubmitterId = :voteSubmitterId RETURNING *"
    )
    fun update(@Bind submissionId: Int, voteSubmitterId: Int, vote: Boolean): VoteDto

    @SqlQuery("DELETE FROM $table" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voteSubmitterId RETURNING *")
    fun delete(@Bind submissionId: Int, voteSubmitterId: Int): VoteDto

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllById(submissionId: Int): List<VoteDto>
}