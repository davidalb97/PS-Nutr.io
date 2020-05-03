package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealIngredientDto

private const val table = "MealIngredient"
private const val mealId = "meal_submission_id"
private const val ingredientId = "ingredient_submission_id"

interface MealIngredientDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<MealIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getByMealId(@Bind mealId: Int): List<MealIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $ingredientId = :ingredientId")
    fun getByIngredientId(@Bind ingredientId: Int): List<MealIngredientDto>

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId)" +
            " VALUES(:mealId, :ingredientId) RETURNING *")
    fun insert(@Bind mealId: Int, @Bind ingredientId: Int): MealIngredientDto

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [mealId, ingredientId]
    ) values: List<MealIngredientParam>): List<MealIngredientDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submissionId RETURNING *")
    fun deleteAllByMealId(submissionId: Int): List<MealIngredientDto>
}

//Variable names must match sql columns
data class MealIngredientParam(val meal_submission_id: Int, val ingredient_submission_id: Int)