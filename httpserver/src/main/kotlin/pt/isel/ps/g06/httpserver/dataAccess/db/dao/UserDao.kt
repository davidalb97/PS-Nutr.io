package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.UserMapper

@RegisterRowMapper(UserMapper::class)
interface UserDao {

    companion object {
        const val table = "_User"
        const val id = "submitter_id"
        const val email = "email"
        const val sessionSecret = "session_secret"
        const val date = "creation_date"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbUserDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): DbUserDto?

    @SqlQuery("SELECT * FROM $table WHERE $email = :email")
    fun getByEmail(@Bind email: String): DbUserDto?

    @SqlQuery("INSERT INTO $table($id, $email, $sessionSecret) " +
            "VALUES(:submitter_id, :email, :session_secret) RETURNING *")
    fun insert(@Bind submitter_id: Int, @Bind email: String, @Bind sessionSecret: String): DbUserDto
}