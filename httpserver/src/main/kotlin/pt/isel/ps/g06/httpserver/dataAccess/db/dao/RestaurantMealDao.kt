package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto

interface RestaurantMealDao {

    companion object {
        const val table = "RestaurantMeal"
        const val id = "submission_id"
        const val mealId = "meal_submission_id"
        const val restaurantId = "restaurant_submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbRestaurantMealDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(submissionId: Int): DbRestaurantMealDto?

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId AND $mealId = :mealId")
    fun getByRestaurantAndMealId(@Bind restaurantId: Int, @Bind mealId: Int): DbRestaurantMealDto?

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getAllByMealId(@Bind mealId: Int): List<DbRestaurantMealDto>

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId")
    fun getAllByRestaurantId(@Bind restaurantId: Int): Collection<DbRestaurantMealDto>

    @SqlQuery("INSERT INTO $table($id, $restaurantId, $mealId)" +
            " VALUES(:submissionId, :restaurantId, :mealId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind restaurantId: Int, @Bind mealId: Int): DbRestaurantMealDto

    @SqlQuery("DELETE FROM $table WHERE $restaurantId = :restaurantId RETURNING *")
    fun deleteAllByRestaurantId(@Bind restaurantId: Int): List<DbRestaurantMealDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :mealId RETURNING *")
    fun deleteAllByMealId(@Bind mealId: Int): List<DbRestaurantMealDto>

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun deleteById(@Bind submissionId: Int): DbRestaurantMealDto
}