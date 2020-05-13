package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealIngredientDto

interface MealIngredientDao {

    companion object {
        const val table = "MealIngredient"
        const val mealId = "meal_submission_id"
        const val ingredientId = "ingredient_submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<MealIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getAllByMealId(@Bind mealId: Int): List<MealIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $ingredientId = :ingredientId")
    fun getAllByIngredientId(@Bind ingredientId: Int): List<MealIngredientDto>

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId)" +
            " VALUES(:mealId, :ingredientId) RETURNING *")
    fun insert(@Bind mealId: Int, @Bind ingredientId: Int): MealIngredientDto

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId) values <mealIngredientParams> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [mealId, ingredientId])
                  mealIngredientParams: List<MealIngredientParam>
    ): List<MealIngredientDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submissionId RETURNING *")
    fun deleteAllByMealId(submissionId: Int): List<MealIngredientDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $mealId = :submissionId" +
            " AND $ingredientId in <deleteIngredientIds> RETURNING *")
    fun deleteAllByMealIdAndIngredientIds(
            @Bind submissionId: Int,
            @BindList deleteIngredientIds: List<Int>
    ): List<MealIngredientDto>
}

//Variable names must match sql columns
data class MealIngredientParam(val meal_submission_id: Int, val ingredient_submission_id: Int)