package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto

@Repository
class UserDbRepository(private val databaseContext: DatabaseContext) {

    fun getBySubmitter(submitterId: Int): DbUserDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .findBySubmitterId(submitterId)
        }
    }

    fun getByEmail(email: String): DbUserDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .findByEmail(email)
        }
    }

    fun insertUser(submitterId: Int, email: String, username: String, password: String): DbUserDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .insertUser(submitterId, email, username, password)
        }
    }

    fun deleteUser(email: String): DbUserDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .deleteUserByEmail(email)
        }
    }

    fun updateUserBan(submitterId: Int, isBanned: Boolean): DbUserDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(UserDao::class.java)
                    .updateUserBan(submitterId, isBanned)
        }
    }
}