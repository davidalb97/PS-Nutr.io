package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealIngredientDto

//Submission constants
private const val S_table = SubmissionDao.table
private const val S_submission_id = SubmissionDao.id
private const val S_submission_type = SubmissionDao.type

private const val INGREDIENT = "Ingredient"
private const val MEAL = "Meal"

interface MealIngredientDao {

    companion object {
        const val table = "MealIngredient"
        const val mealId = "meal_submission_id"
        const val ingredientId = "ingredient_submission_id"
        const val quantity = "quantity"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbMealIngredientDto>


    @SqlQuery("SELECT * " +
            "FROM $table " +
            "INNER JOIN $S_table " +
            "ON $S_table.$S_submission_id = $table.$ingredientId " +
            "WHERE $S_table.$S_submission_type = '$INGREDIENT' " +
            "AND $table.$mealId = :mealId"
    )
    fun getMealIngredients(@Bind mealId: Int): Collection<DbMealIngredientDto>

    @SqlQuery("SELECT * " +
            "FROM $table " +
            "INNER JOIN $S_table " +
            "ON $S_table.$S_submission_id = $table.$ingredientId " +
            "WHERE $S_table.$S_submission_type = '$MEAL' " +
            "AND $table.$mealId = :mealId"
    )
    fun getMealComponents(@Bind mealId: Int): Collection<DbMealIngredientDto>

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId)" +
            " VALUES(:mealId, :ingredientId) RETURNING *")
    fun insert(@Bind mealId: Int, @Bind ingredientId: Int): DbMealIngredientDto

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId, $quantity) values <mealIngredientParams> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [mealId, ingredientId, quantity])
                  mealIngredientParams: Collection<DbMealIngredientDto>
    ): List<DbMealIngredientDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submissionId RETURNING *")
    fun deleteAllByMealId(submissionId: Int): List<DbMealIngredientDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $mealId = :submissionId" +
            " AND $ingredientId in (<deleteIngredientIds>) RETURNING *")
    fun deleteAllByMealIdAndIngredientIds(
            @Bind submissionId: Int,
            @BindList deleteIngredientIds: List<Int>
    ): List<DbMealIngredientDto>
}