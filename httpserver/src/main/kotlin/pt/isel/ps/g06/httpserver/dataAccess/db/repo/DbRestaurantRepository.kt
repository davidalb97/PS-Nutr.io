package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurant
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantDao

@Repository
class DbRestaurantRepository(private val jdbi: Jdbi) {

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float, radius: Int): List<DbRestaurant> {
        return jdbi.open().use {
            it.attach(RestaurantDao::class.java).getRestaurantsByCoordinates(longitude, latitude, radius)
        }
    }
}