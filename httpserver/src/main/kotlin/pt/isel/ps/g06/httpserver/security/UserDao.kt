package pt.isel.ps.g06.httpserver.security

import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/*@Service
interface UserDao : JpaRepository<UserAuthRequest, Long> {

    companion object {
        const val table = "_User"
        const val username = "name"
        const val email = "email"
        const val password = "password"
    }

    @SqlQuery("SELECT * FROM $table WHERE $username = :username")
    fun findByUsername(username: String?): User

    @SqlQuery("SELECT * FROM $table WHERE $email = :email")
    fun findByEmail(email: String?): User

    @SqlQuery("INSERT INTO $table($email, $username, $password)" +
            " VALUES(:email, :username, :password) RETURNING *")
    fun insertUser(email: String?, username: String?, password: String?): User

    @SqlQuery("DELETE FROM $table WHERE $username=:username")
    fun deleteUserByUsername(username: String?)
}*/
