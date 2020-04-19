package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurant
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantDao

@Repository
class DbRestaurantRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val restaurantDao = RestaurantDao::class.java

    fun getRestaurantById(id: Int): DbRestaurant {
        return jdbi.open().use { handle ->
            return handle.inTransaction<DbRestaurant, Exception>(serializable) {
                it.attach(restaurantDao).getById(id)
            }
        }
    }

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float, radius: Int): List<DbRestaurant> {
        return jdbi.open().use { handle ->
            return handle.inTransaction<List<DbRestaurant>, Exception>(serializable) {
                it.attach(restaurantDao).getByCoordinates(latitude, longitude, radius)
            }
        }
    }

    fun insertRestaurant(submissionId: Int, restaurantName: String, latitude: Float, longitude: Float): Int {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Int, Exception>(serializable) {
                it.attach(restaurantDao).insert(submissionId, restaurantName, latitude, longitude)
            }
        }
    }
}