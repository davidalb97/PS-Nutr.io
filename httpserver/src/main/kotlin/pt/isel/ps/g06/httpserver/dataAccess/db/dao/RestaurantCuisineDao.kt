package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurantCuisine
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbRestaurantMealPortion

private const val table = "RestaurantCuisine"
private const val restaurantId = "restaurant_submission_id"
private const val cuisineName = "cuisine_name"

interface RestaurantCuisineDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbRestaurantMealPortion>

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId")
    fun getByRestaurantId(@Bind restaurantId: Int): DbRestaurantCuisine

    @SqlQuery("SELECT * FROM $table WHERE $cuisineName = :cuisineName")
    fun getByCuisineName(@Bind cuisineName: String): DbRestaurantCuisine

    @SqlQuery("INSERT INTO $table($restaurantId, $cuisineName) VALUES(:restaurantId, :cuisineName)")
    fun insert(@Bind restaurantId: Int, @Bind cuisineName: String)
}