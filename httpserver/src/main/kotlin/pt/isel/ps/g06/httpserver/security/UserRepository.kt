package pt.isel.ps.g06.httpserver.security

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.BaseDbRepo
import pt.isel.ps.g06.httpserver.security.dto.UserDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class UserRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getBySubmitterId(submitterId: Int): UserDto? {
        return jdbi.inTransaction<UserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .findBySubmitterId(submitterId)
        }
    }

    fun getByEmail(email: String): UserDto? {
        return jdbi.inTransaction<UserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .findByEmail(email)
        }
    }

    fun insertUser(email: String, name: String, password: String): UserDto? {
        return jdbi.inTransaction<UserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .insertUser(email, name, password)
        }
    }

    fun deleteUser(email: String): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .deleteUserByEmail(email)
        }
    }
}