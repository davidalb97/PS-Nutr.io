package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCustomMealDto

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

interface CustomMealDao {
    companion object {
        const val table = "CustomMeal"
        const val id = "submission_id"
        const val mealName = "meal_name"
        const val mealPortion = "meal_portion"
        const val carbAmount = "carb_amount"
        const val imageUrl = "image_url"
    }

    @SqlQuery("SELECT * FROM $table " +
            "INNER JOIN $SS_table " +
            "ON $table.$id = $SS_table.$SS_submissionId " +
            "WHERE $SS_table.$SS_submitterId = :submitterId")
    fun getAllUserCustomMeals(@Bind submitterId: Int): Sequence<DbCustomMealDto>

    @SqlQuery("SELECT * FROM $table " +
            "INNER JOIN $SS_table " +
            "ON $table.$id = $SS_table.$SS_submissionId " +
            "WHERE $SS_table.$SS_submitterId = :submitterId" +
            "AND $table.$mealName = :mealName")
    fun getUserCustomMeal(@Bind submitterId: Int, @Bind mealName: String): DbCustomMealDto

    @SqlQuery("INSERT INTO $table($id, $mealName, $mealPortion, $carbAmount, $imageUrl) " +
            "VALUES(:submissionId, :mealName, :mealPortion, :carbAmount, :imageUrl) " +
            "RETURNING *")
    fun insertUserCustomMeal(
            @Bind submissionId: Int,
            @Bind mealName: String,
            @Bind mealPortion: Int,
            @Bind carbAmount: Int,
            @Bind imageUrl: String
    ): DbCustomMealDto

    @SqlQuery("DELETE FROM $table WHERE $table.$mealName = :mealName RETURNING *")
    fun deleteUserCustomMeal(@Bind mealName: String): DbCustomMealDto
}