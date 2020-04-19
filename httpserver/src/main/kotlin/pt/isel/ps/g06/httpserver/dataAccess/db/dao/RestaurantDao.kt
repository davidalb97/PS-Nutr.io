package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurant

private const val table = "Restaurant"
private const val id = "restaurant_id"
private const val name = "restaurant_name"
private const val latitude = "latitude"
private const val longitude = "longitude"

interface RestaurantDao {

    //TODO! must call geo-location function!
    @SqlQuery("SELECT * FROM $table WHERE geoloc($latitude, $longitude) < :radius")
    fun getByCoordinates(@Bind latitude: Float, @Bind longitude: Float, radius: Int): List<DbRestaurant>

    @SqlQuery("SELECT * FROM $table WHERE $id = :restaurantId")
    @Transaction(TransactionIsolationLevel.SERIALIZABLE)
    fun getById(@Bind restaurantId: Int): DbRestaurant?

    @SqlQuery("INSERT INTO $table($id, $name, $latitude, $longitude) VALUES(:submission_id, :restaurant_name, :latitude, :longitude)")
    fun insert(@Bind submission_id: String, @Bind restaurant_name: String, @Bind latitue: Float, @Bind longitude: Float)
}