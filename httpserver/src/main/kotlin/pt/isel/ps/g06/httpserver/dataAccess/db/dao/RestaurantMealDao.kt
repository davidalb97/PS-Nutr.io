package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto

private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submission_id = SubmissionSubmitterDao.submissionId

interface RestaurantMealDao {

    companion object {
        const val table = "RestaurantMeal"
        const val id = "submission_id"
        const val mealId = "meal_submission_id"
        const val restaurantId = "restaurant_submission_id"
        const val verified = "verified"
    }

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId AND $mealId = :mealId")
    fun getByRestaurantAndMealId(@Bind restaurantId: Int, @Bind mealId: Int): DbRestaurantMealDto?

    @SqlQuery("SELECT * " +
            "FROM $table " +
            "INNER JOIN $SS_table " +
            "ON $table.$id = $SS_table.$SS_submission_id " +
            "WHERE $table.$restaurantId = :restaurantId")
    fun getAllUserMealsByRestaurantId(@Bind restaurantId: Int): Collection<DbRestaurantMealDto>

    @SqlQuery("INSERT INTO $table($id, $restaurantId, $mealId, $verified)" +
            " VALUES(:submissionId, :restaurantId, :mealId, :verified) RETURNING *")
    fun insert(
            @Bind submissionId: Int,
            @Bind restaurantId: Int,
            @Bind mealId: Int,
            @Bind verified: Boolean
    ): DbRestaurantMealDto

    @SqlQuery("UPDATE $table SET $verified = :verified WHERE $id = :submissionId  RETURNING *")
    fun updateRestaurantMealVerification(@Bind submissionId: Int, verified: Boolean): DbRestaurantMealDto

    @SqlQuery("DELETE FROM $table WHERE $restaurantId = :restaurantId RETURNING *")
    fun deleteAllByRestaurantId(@Bind restaurantId: Int): List<DbRestaurantMealDto>

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun deleteById(@Bind submissionId: Int): DbRestaurantMealDto
}