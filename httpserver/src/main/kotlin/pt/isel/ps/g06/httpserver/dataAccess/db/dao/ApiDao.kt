package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbAPI

private const val table = "API"
private const val id = "submitter_id"
private const val apiToken = "api_token"

interface ApiDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbAPI>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): DbAPI

    @SqlQuery("INSERT INTO $table($id, $apiToken) " +
            "VALUES(:submitterId, :apiToken)")
    fun insert(@Bind submitterId: Int, @Bind apiToken: String)
}