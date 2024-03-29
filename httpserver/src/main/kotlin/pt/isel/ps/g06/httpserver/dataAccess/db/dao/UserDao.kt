package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.common.NORMAL_USER
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto

private const val S_table = SubmitterDao.table
private const val S_id = SubmitterDao.id
private const val S_date = SubmitterDao.date


interface UserDao {
    companion object {
        const val table = "_User"
        const val submitterId = "submitter_id"
        const val email = "email"
        const val username = "username"
        const val password = "password"
        const val role = "role"
        const val isBanned = "is_banned"
    }

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId")
    fun findBySubmitterId(submitterId: Int): DbUserDto?

    @SqlQuery("SELECT * FROM $table WHERE $email = :email")
    fun findByEmail(email: String): DbUserDto?

    @SqlQuery("INSERT INTO $table($submitterId, $email, $username, $password, $role, $isBanned)" +
            " VALUES(:submitterId, :email, :username, :password, :role, :isBanned) RETURNING *")
    fun insertUser(
            submitterId: Int,
            email: String,
            username: String,
            password: String,
            role: String = NORMAL_USER,
            isBanned: Boolean = false
    ): DbUserDto

    @SqlQuery("DELETE FROM $table WHERE $email=:email RETURNING *")
    fun deleteUserByEmail(email: String): DbUserDto?

    @SqlQuery("UPDATE $table SET $isBanned=:isBanned WHERE $submitterId = :submitterId RETURNING *")
    fun updateUserBan(submitterId: Int, isBanned: Boolean): DbUserDto?
}