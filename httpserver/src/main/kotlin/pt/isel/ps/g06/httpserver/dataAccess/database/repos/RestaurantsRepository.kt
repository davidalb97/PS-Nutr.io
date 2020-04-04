package pt.isel.ps.g06.httpserver.dataAccess.database.repos

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.database.daos.RestaurantDao
import pt.isel.ps.g06.httpserver.dataAccess.database.model.DbRestaurant

@Repository
class RestaurantsRepository(private val jdbi: Jdbi) {

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float, radius: Int): List<DbRestaurant> {
        return jdbi.open().use { it.attach(RestaurantDao::class.java).getRestaurantsByCoordinates(longitude, latitude, radius) }
    }
}