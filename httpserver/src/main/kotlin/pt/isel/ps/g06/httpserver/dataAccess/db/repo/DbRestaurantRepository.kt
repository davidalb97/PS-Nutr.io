package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurant
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantDao

@Repository
class DbRestaurantRepository(private val jdbi: Jdbi) {

    val handle: Handle = jdbi.open()
    val restaurantDao = RestaurantDao::class.java

    fun getRestaurantById(id: Int) {
        return handle.use {
            it.attach(restaurantDao).getRestaurantById(id)
        }
    }

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float, radius: Int): List<DbRestaurant> {
        return handle.use {
            it.attach(restaurantDao).getRestaurantsByCoordinates(longitude, latitude, radius)
        }
    }


}