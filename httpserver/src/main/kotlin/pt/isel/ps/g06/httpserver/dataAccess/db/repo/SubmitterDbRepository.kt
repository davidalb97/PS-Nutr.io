package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

@Repository
class SubmitterDbRepository(private val jdbi: Jdbi) {
    fun getApiSubmitters(): Collection<DbSubmitterDto> {
        return jdbi.inTransaction<Collection<DbSubmitterDto>, Exception> {
            //TODO 1. Hardcoded string needs to be replaced by enum
            //TODO 2. Type should not be API, but Restaurant API instead.
            return@inTransaction it.attach(SubmitterDao::class.java).getAllByType("API")
        }
    }
}