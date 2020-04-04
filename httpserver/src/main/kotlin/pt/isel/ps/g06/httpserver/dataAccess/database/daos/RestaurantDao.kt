package pt.isel.ps.g06.httpserver.dataAccess.database.daos

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.database.model.DbRestaurant

interface RestaurantDao {

    @SqlQuery("SELECT restaurant_name FROM Restaurant WHERE latitude < :latitude AND longitude < :longitude")
    @RegisterKotlinMapper(DbRestaurant::class)
    fun getRestaurantsByCoordinates(@Bind latitude: Float, @Bind longitude: Float): List<DbRestaurant>
}