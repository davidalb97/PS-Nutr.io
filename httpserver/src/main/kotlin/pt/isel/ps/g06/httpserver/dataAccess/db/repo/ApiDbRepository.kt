package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ApiDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val apiDaoClass = ApiDao::class.java

@Repository
class ApiDbRepository (jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getApisByName(apiNames: Collection<String>): Collection<DbApiDto> {
        return jdbi.inTransaction<Collection<DbApiDto>, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(apiDaoClass)
                    .getApiSubmittersByName(apiNames)
        }
    }

    fun getApiBySubmitterId(submitterId: Int): DbApiDto? {
        return jdbi.inTransaction<DbApiDto, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(apiDaoClass)
                    .getById(submitterId)
        }
    }
}