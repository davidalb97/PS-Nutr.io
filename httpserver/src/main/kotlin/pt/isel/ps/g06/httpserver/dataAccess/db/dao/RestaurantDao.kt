package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurant

private const val restaurant = "Restaurant"

interface RestaurantDao {

    @SqlQuery("SELECT * FROM $restaurant")
    fun getRestaurantsByCoordinates(@Bind latitude: Float, @Bind longitude: Float, radius: Int): List<DbRestaurant>

    @SqlQuery("SELECT * FROM $restaurant WHERE restaurant_id = :restaurantId")
    @Transaction(TransactionIsolationLevel.SERIALIZABLE)
    fun getRestaurantById(@Bind restaurantId: Int): DbRestaurant?
}