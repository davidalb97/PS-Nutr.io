package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.UserDto

private const val table = "Submitter"
private const val id = "submitter_id"
private const val name = "submitter_name"
private const val type = "submitter_type"

interface SubmitterDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): SubmitterDto?

    @SqlQuery("INSERT INTO $table($name, $type) " +
            "VALUES(:name, :type) RETURNING *")
    fun insert(@Bind name: String, @Bind type: String): SubmitterDto
}