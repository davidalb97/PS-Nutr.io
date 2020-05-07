package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val cuisineDaoClass = CuisineDao::class.java

class CuisineDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getByName(name: String): List<CuisineDto> {
        return jdbi.inTransaction<List<CuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass).getByName(name)
        }
    }

    fun insert(name: String): CuisineDto {
        return jdbi.inTransaction<CuisineDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass).insert(name)
        }
    }
}