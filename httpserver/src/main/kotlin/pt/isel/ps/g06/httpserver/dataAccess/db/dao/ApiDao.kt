package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiDto

//API table constants
private const val table = "Api"
private const val id = "submitter_id"
private const val apiToken = "api_token"

//Submitter table constants
private const val submitterTable = "Submitter"
private const val submitterName = "submitter_name"
private const val submitterId = "submitter_id"

interface ApiDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<ApiDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): ApiDto?

    @SqlQuery("SELECT $submitterTable.$submitterId, $table.$apiToken" +
            " FROM $submitterTable" +
            " INNER JOIN $table" +
            " ON $submitterTable.$submitterId = $table.$id" +
            " WHERE $submitterName = :submitterName"
    )
    fun getByName(@Bind submitterName: String): ApiDto?

    @SqlQuery("INSERT INTO $table($id, $apiToken) " +
            "VALUES(:submitterId, :apiToken) RETURNING *")
    fun insert(@Bind submitterId: Int, @Bind apiToken: String): ApiDto
}