package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbFavoriteDto
import java.util.stream.Stream

interface FavoriteDao {

    companion object {
        const val table = "Favorite"
        const val submissionId = "submission_id"
        const val submitterId = "submitter_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): Stream<DbFavoriteDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId AND $submitterId = :userId")
    fun getByIds(submissionId: Int, userId: Int): DbFavoriteDto?

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getAllBySubmissionId(submissionId: Int): Stream<DbFavoriteDto>

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :userId")
    fun getAllBySubmitterId(userId: Int): Stream<DbFavoriteDto>

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId)" +
            " VALUES(:submissionId, :submitterId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind submitterId: Int): DbFavoriteDto

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllBySubmissionId(submissionId: Int): Stream<DbFavoriteDto>
}