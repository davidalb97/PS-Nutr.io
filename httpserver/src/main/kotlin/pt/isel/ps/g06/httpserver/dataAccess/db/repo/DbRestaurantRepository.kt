package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurant
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantDao

@Repository
class DbRestaurantRepository(private val jdbi: Jdbi) {

    val restaurantDao = RestaurantDao::class.java

    fun getRestaurantById(id: Int) {
        return jdbi.open().use {
            it.attach(restaurantDao).getById(id)
        }
    }

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float, radius: Int): List<DbRestaurant> {
        return jdbi.open().use {
            it.attach(restaurantDao).getByCoordinates(longitude, latitude, radius)
        }
    }
}