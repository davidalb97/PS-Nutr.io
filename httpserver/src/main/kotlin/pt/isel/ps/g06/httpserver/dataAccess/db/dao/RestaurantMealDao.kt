package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto

//SubmissionSubmitter constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submission_id = SubmissionSubmitterDao.submissionId

//Favorite constants
private const val F_table = FavoriteDao.table
private const val F_submitter_id = FavoriteDao.submitterId
private const val F_submission_id = FavoriteDao.submissionId

interface RestaurantMealDao {

    companion object {
        const val table = "RestaurantMeal"
        const val id = "submission_id"
        const val mealId = "meal_submission_id"
        const val restaurantId = "restaurant_submission_id"
        const val verified = "verified"
        const val attributes = "$table.$id, $table.$restaurantId, $table.$mealId, $table.$verified"
    }

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getAllByMealId(@Bind mealId: Int): ResultIterable<DbRestaurantMealDto>

    @SqlQuery("SELECT * FROM $table WHERE $restaurantId = :restaurantId AND $mealId = :mealId")
    fun getByRestaurantAndMealId(@Bind restaurantId: Int, @Bind mealId: Int): DbRestaurantMealDto?

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "INNER JOIN $F_table " +
            "ON $F_table.$F_submission_id = $table.$id " +
            "WHERE $F_table.$F_submitter_id = :submitterId " +
            "LIMIT :count " +
            "OFFSET :skip * :count"
    )
    fun getAllUserFavorites(
            @Bind submitterId: Int,
            @Bind count: Int?,
            @Bind skip: Int?
    ): ResultIterable<DbRestaurantMealDto>

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

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun deleteById(@Bind submissionId: Int): DbRestaurantMealDto
}