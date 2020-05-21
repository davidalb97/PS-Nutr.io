package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealPortionDto

interface RestaurantMealPortionDao {

    companion object {
        const val table = "RestaurantMealPortion"
        const val mealId = "meal_submission_id"
        const val portionId = "portion_submission_id"
        const val restaurantId = "restaurant_submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbRestaurantMealPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getAllByMealId(@Bind mealId: Int): List<DbRestaurantMealPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $portionId = :portionId")
    fun getByPortionId(@Bind portionId: Int): DbRestaurantMealPortionDto?

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId")
    fun getAllByRestaurantId(@Bind restaurantId: Int): List<DbRestaurantMealPortionDto>

    @SqlQuery("INSERT INTO $table($mealId, $portionId, $restaurantId)" +
            " VALUES(:mealId, :portionId, :restaurantId) RETURNING *")
    fun insert(@Bind mealId: Int, @Bind portionId: Int, restaurantId: Int): DbRestaurantMealPortionDto

    @SqlQuery("DELETE FROM $table WHERE $restaurantId = :restaurantId RETURNING *")
    fun deleteAllByRestaurantId(@Bind restaurantId: Int): List<DbRestaurantMealPortionDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :mealId RETURNING *")
    fun deleteAllByMealId(@Bind mealId: Int): DbRestaurantMealPortionDto
}