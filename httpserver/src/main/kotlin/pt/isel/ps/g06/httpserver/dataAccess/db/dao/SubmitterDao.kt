package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmitterDto

interface SubmitterDao {

    companion object {
        const val table = "Submitter"
        const val id = "submitter_id"
        const val name = "submitter_name"
        const val type = "submitter_type"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): SubmitterDto?

    @SqlQuery("SELECT * FROM $table WHERE $type = :submitterType")
    fun getAllByType(submitterType: String): List<SubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getBySubmissionId(submissionId: Int): SubmitterDto?

    @SqlQuery("INSERT INTO $table($name, $type) " +
            "VALUES(:name, :type) RETURNING *")
    fun insert(@Bind name: String, @Bind type: String): SubmitterDto
}