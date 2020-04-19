package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbCuisine
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurant
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantDao

class DbCuisineRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val cuisineDao = CuisineDao::class.java

    fun insertCuisine(name: String): Int {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Int, Exception>(serializable) {
                it.attach(cuisineDao).insert(name)
            }
        }
    }

    fun getCuisineById(name: String): List<DbCuisine> {
        return jdbi.open().use { handle ->
            return handle.inTransaction<List<DbCuisine>, Exception>(serializable) {
                it.attach(cuisineDao).getByName(name)
            }
        }
    }
}