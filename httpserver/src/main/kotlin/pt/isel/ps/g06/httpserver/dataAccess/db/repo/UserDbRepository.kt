package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class UserDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getBySubmitterId(submitterId: Int): DbUserDto? {
        return jdbi.inTransaction<DbUserDto?, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(UserDao::class.java)
                    .getById(submitterId)
        }
    }
}