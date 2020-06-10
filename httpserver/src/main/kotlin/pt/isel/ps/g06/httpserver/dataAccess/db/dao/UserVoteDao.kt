package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserVoteDto

interface UserVoteDao {

    companion object {
        const val table = "Vote"
        const val submissionId = "submission_id"
        const val voterSubmitterId = "vote_submitter_id"
        const val vote = "vote"
    }

    @SqlQuery("SELECT $vote" +
            " FROM $table" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voteSubmitterId"
    )
    fun getVoteByIds(@Bind submissionId: Int, voteSubmitterId: Int): Boolean?

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getAllById(@Bind submissionId: Int): List<DbUserVoteDto>

    @SqlQuery("INSERT INTO $table($submissionId, $voterSubmitterId, $vote)" +
            " VALUES(:voteSubmissionId, :voteSubmitterId, :vote) RETURNING *")
    fun insert(@Bind voteSubmissionId: Int, voterSubmitterId: Int, vote: Boolean): DbUserVoteDto

    @SqlQuery("UPDATE $table" +
            " SET $vote = :vote" +
            " WHERE $submissionId =" +
            " :submissionId, $voterSubmitterId = :voteSubmitterId RETURNING *"
    )
    fun update(@Bind submissionId: Int, voteSubmitterId: Int, vote: Boolean): DbUserVoteDto

    @SqlQuery("DELETE FROM $table" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voteSubmitterId RETURNING *")
    fun delete(@Bind submissionId: Int, voteSubmitterId: Int): DbUserVoteDto

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllById(submissionId: Int): List<DbUserVoteDto>
}