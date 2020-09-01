package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ApiDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiDto
import java.util.stream.Stream

private val apiDaoClass = ApiDao::class.java

@Repository
class ApiDbRepository(private val databaseContext: DatabaseContext) {

    fun getApisByName(apiNames: Collection<String>): Stream<DbApiDto> {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(apiDaoClass)
                    .getApiSubmittersByName(apiNames)
        }
    }

    fun getApiBySubmitterId(submitterId: Int): DbApiDto? {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(apiDaoClass)
                    .getById(submitterId)
        }
    }
}