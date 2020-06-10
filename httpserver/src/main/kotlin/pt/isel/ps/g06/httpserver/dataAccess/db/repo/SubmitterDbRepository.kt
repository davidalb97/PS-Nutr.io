package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

@Repository
class SubmitterDbRepository(private val jdbi: Jdbi) {
    fun getApiSubmittersByName(names: Collection<String>): Collection<DbSubmitterDto> {
        return jdbi.inTransaction<Collection<DbSubmitterDto>, Exception> {
            return@inTransaction it.attach(SubmitterDao::class.java).getApiSubmittersByName(names)
        }
    }
}