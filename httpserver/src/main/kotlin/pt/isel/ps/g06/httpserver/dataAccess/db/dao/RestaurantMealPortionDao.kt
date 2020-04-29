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
    fun getAllByMealId(@Bind mealId: Int): List<RestaurantMealPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $portionId = :portionId")
    fun getByPortionId(@Bind portionId: Int): RestaurantMealPortionDto?

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId")
    fun getAllByRestaurantId(@Bind restaurantId: Int): List<RestaurantMealPortionDto>

    @SqlQuery("INSERT INTO $table($mealId, $portionId, $restaurantId)" +
            " VALUES(:mealId, :portionId, :restaurantId) RETURNING *")
    fun insert(@Bind mealId: Int, @Bind portionId: Int, restaurantId: Int): RestaurantMealPortionDto

    @SqlQuery("DELETE FROM $table WHERE $restaurantId = :restaurantId RETURNING *")
    fun deleteFromRestaurant(@Bind restaurantId: Int): RestaurantMealPortionDto

    @SqlQuery("DELETE FROM $table WHERE $mealId = :mealId RETURNING *")
    fun deleteFromMeal(@Bind mealId: Int): RestaurantMealPortionDto
}