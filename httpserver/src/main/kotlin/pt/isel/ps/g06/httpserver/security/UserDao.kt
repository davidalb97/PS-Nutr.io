package pt.isel.ps.g06.httpserver.security

import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.security.dto.UserDto

interface UserDao {

    companion object {
        const val table = "_User"
        const val submitterId = "submitter_id"
        const val email = "email"
        const val password = "password"
    }

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId")
    fun findBySubmitterId(submitterId: Int?): UserDto

    @SqlQuery("SELECT * FROM $table WHERE $email = :email")
    fun findByEmail(email: String?): UserDto

    @SqlQuery("INSERT INTO $table($submitterId, $email, $password)" +
            " VALUES(:submitterId, :email, :password) RETURNING *")
    fun insertUser(submitterId: Int?, email: String?, password: String?): UserDto

    @SqlQuery("DELETE FROM $table WHERE $email=:email")
    fun deleteUserByEmail(email: String?): Boolean
}
