package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserVoteDto
import java.util.stream.Stream

interface UserVoteDao {

    companion object {
        const val table = "UserVote"
        const val submissionId = "submission_id"
        const val voterSubmitterId = "vote_submitter_id"
        const val vote = "vote"
    }

    @SqlQuery("SELECT *" +
            " FROM $table" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voterSubmitterId"
    )
    fun getUserVoteForSubmission(@Bind submissionId: Int, @Bind voterSubmitterId: Int): DbUserVoteDto?

    @SqlQuery("INSERT INTO $table($submissionId, $voterSubmitterId, $vote)" +
            " VALUES(:voteSubmissionId, :voterSubmitterId, :vote) RETURNING *")
    fun insert(@Bind voteSubmissionId: Int, @Bind voterSubmitterId: Int, @Bind vote: Boolean): DbUserVoteDto

    @SqlQuery("UPDATE $table" +
            " SET $vote = :vote" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voterSubmitterId RETURNING *"
    )
    fun update(@Bind submissionId: Int, @Bind voterSubmitterId: Int, vote: Boolean): DbUserVoteDto

    @SqlQuery("DELETE FROM $table" +
            " WHERE $submissionId = :submissionId" +
            " AND $voterSubmitterId = :voterSubmitterId RETURNING *")
    fun delete(@Bind submissionId: Int, @Bind voterSubmitterId: Int): DbUserVoteDto

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllById(@Bind submissionId: Int): Stream<DbUserVoteDto>
}