package pt.isel.ps.g06.httpserver.dataAccess.database.daos

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface RestaurantDao {

    @SqlQuery("SELECT restaurant_name FROM Restaurant WHERE latitude < :latitude AND longitude < :longitude")
    fun getRestaurantsByCoordinates(@Bind latitude: Float, @Bind longitude: Float): List<String>
}