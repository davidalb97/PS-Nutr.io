package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbFavoriteDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionSubmitterDto

interface FavoriteDao {

    companion object {
        const val table = "Favorite"
        const val submissionId = "submission_id"
        const val submitterId = "submitter_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbFavoriteDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getAllBySubmissionId(submissionId: Int): List<DbFavoriteDto>

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId")
    fun getAllBySubmitterId(submitterId: Int): List<DbFavoriteDto>

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId)" +
            " VALUES(:submissionId, :submitterId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind submitterId: Int): DbFavoriteDto

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllBySubmissionId(submissionId: Int): List<DbFavoriteDto>
}