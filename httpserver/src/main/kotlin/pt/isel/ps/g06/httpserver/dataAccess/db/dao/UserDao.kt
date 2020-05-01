package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.UserDto

private const val table = "_User"
private const val id = "submitter_id"
private const val email = "email"
private const val sessionSecret = "session_secret"

interface UserDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<UserDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): UserDto

    @SqlQuery("SELECT * FROM $table WHERE $email = :email")
    fun getByEmail(@Bind email: String): UserDto

    @SqlQuery("INSERT INTO $table($id, $email, $sessionSecret) " +
            "VALUES(:submitter_id, :email, :session_secret)")
    fun insert(@Bind submitter_id: Int, @Bind email: String)
}