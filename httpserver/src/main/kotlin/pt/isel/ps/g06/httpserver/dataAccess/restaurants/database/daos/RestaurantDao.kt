package pt.isel.ps.g06.httpserver.dataAccess.restaurants.database.daos

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.restaurants.database.model.DbRestaurant

//const val distance = "earth_distance(ll_to_earth(latitude, longitude), ll_to_earth(:latitude, :longitude))"
//const val sqlQueryStr = "SELECT *, $distance FROM Restaurant WHERE $distance < :radius"
interface RestaurantDao {

    @SqlQuery("SELECT * FROM Restaurant")
    fun getRestaurantsByCoordinates(@Bind latitude: Float, @Bind longitude: Float, radius: Int): List<DbRestaurant>
}