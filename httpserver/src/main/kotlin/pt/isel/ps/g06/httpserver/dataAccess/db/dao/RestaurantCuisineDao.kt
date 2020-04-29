package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantMealPortionDto

private const val table = "RestaurantCuisine"
private const val restaurantId = "restaurant_submission_id"
private const val cuisineName = "cuisine_name"

interface RestaurantCuisineDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<RestaurantMealPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId")
    fun getByRestaurantId(@Bind restaurantId: Int): RestaurantCuisineDto?

    @SqlQuery("SELECT * FROM $table WHERE $cuisineName = :cuisineName")
    fun getByCuisineName(@Bind cuisineName: String): RestaurantCuisineDto?

    @SqlQuery("INSERT INTO $table($restaurantId, $cuisineName)" +
            " VALUES(:restaurantId, :cuisineName) RETURNING *")
    fun insert(@Bind restaurantId: Int, @Bind cuisineName: String): RestaurantCuisineDto

    @SqlQuery("DELETE FROM $table WHERE $restaurantId = :restaurantId")
    fun delete(@Bind restaurantId: Int): Boolean
}