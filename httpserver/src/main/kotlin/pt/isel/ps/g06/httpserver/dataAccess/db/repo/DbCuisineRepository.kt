package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao

class DbCuisineRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val cuisineDao = CuisineDao::class.java

    fun getByName(name: String): List<CuisineDto>? {
        return inTransaction(jdbi, serializable) {
            it.attach(cuisineDao).getByName(name)
        }
    }

    fun insertCuisine(name: String): Boolean {
        return inTransaction(jdbi, serializable) {
            it.attach(cuisineDao).insert(name)
            true
        }
    }

}