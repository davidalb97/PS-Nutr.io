package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbFavoriteDto

interface FavoriteDao {

    companion object {
        const val table = "Favorite"
        const val submissionId = "submission_id"
        const val submitterId = "submitter_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): ResultIterable<DbFavoriteDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId AND $submitterId = :userId")
    fun getByIds(submissionId: Int, userId: Int): DbFavoriteDto?

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId)" +
            " VALUES(:submissionId, :submitterId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind submitterId: Int): DbFavoriteDto

    @SqlQuery("DELETE FROM $table " +
            "WHERE $submissionId = :submissionId AND $submitterId = :submitterId RETURNING *")
    fun delete(@Bind submissionId: Int, @Bind submitterId: Int): DbFavoriteDto
}