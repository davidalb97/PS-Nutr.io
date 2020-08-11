package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class UserRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getBySubmitterName(submitterName: String): DbUserDto? {
        return jdbi.inTransaction<DbUserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .findBySubmitterName(submitterName)
        }
    }

    fun getBySubmitterId(submitterId: Int): DbUserDto? {
        return jdbi.inTransaction<DbUserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .findBySubmitterId(submitterId)
        }
    }

    fun getByEmail(email: String): DbUserDto? {
        return jdbi.inTransaction<DbUserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .findByEmail(email)
        }
    }

    fun insertUser(submitterId: Int, email: String, password: String): DbUserDto? {
        return jdbi.inTransaction<DbUserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .insertUser(submitterId, email, password)
        }
    }

    fun deleteUser(email: String): DbUserDto? {
        return jdbi.inTransaction<DbUserDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .deleteUserByEmail(email)
        }
    }
}