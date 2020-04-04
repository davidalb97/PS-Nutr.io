package pt.isel.ps.g06.httpserver.dataAccess.database.repos

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.database.daos.RestaurantDao

@Repository
class RestaurantsRepository(private val jdbi: Jdbi) {

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float): List<String> {
        return jdbi.open().use { it.attach(RestaurantDao::class.java).getRestaurantsByCoordinates(longitude, latitude) }
    }
}