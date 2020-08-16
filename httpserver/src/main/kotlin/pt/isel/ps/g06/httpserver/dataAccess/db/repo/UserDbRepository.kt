package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto

@Repository
class UserRepository(private val databaseContext: DatabaseContext) {

    fun getBySubmitterName(submitterName: String): DbUserDto? {
        return databaseContext.inTransaction {
            it.attach(UserDao::class.java).findBySubmitterName(submitterName)
        }
    }

    fun getBySubmitterId(submitterId: Int): DbUserDto? {
        return databaseContext.inTransaction {
            it.attach(UserDao::class.java).findBySubmitterId(submitterId)
        }
    }

    fun getByEmail(email: String): DbUserDto? {
        return databaseContext.inTransaction {
            it.attach(UserDao::class.java).findByEmail(email)
        }
    }

    fun insertUser(submitterId: Int, email: String, password: String): DbUserDto? {
        return databaseContext.inTransaction {
            it.attach(UserDao::class.java).insertUser(submitterId, email, password)
        }
    }

    fun deleteUser(email: String): DbUserDto? {
        return databaseContext.inTransaction {
            it.attach(UserDao::class.java).deleteUserByEmail(email)
        }
    }
}