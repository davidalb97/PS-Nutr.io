package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantMealPortionDto

private const val table = "RestaurantMealPortion"
private const val mealId = "meal_submission_id"
private const val portionId = "portion_submission_id"
private const val restaurantId = "restaurant_submission_id"

interface RestaurantMealPortionDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<RestaurantMealPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getByMealId(@Bind mealId: Int): RestaurantMealPortionDto

    @SqlQuery("SELECT * FROM $table WHERE $portionId = :portionId")
    fun getByPortionId(@Bind portionId: Int): RestaurantMealPortionDto

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId")
    fun getByRestaurantId(@Bind restaurantId: Int): RestaurantMealPortionDto

    @SqlQuery("INSERT INTO $table($mealId, $portionId, $restaurantId) VALUES(:mealId, :portionId, :restaurantId)")
    fun insert(@Bind mealId: Int, @Bind portionId: Int, restaurantId: Int)
}