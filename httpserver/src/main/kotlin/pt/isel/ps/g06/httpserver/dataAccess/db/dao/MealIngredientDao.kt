package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.MEAL_TYPE_SUGGESTED_INGREDIENT
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealIngredientDto
import java.util.stream.Stream

private const val M_table = MealDao.table
private const val M_id = MealDao.id
private const val M_type = MealDao.meal_type

interface MealIngredientDao {

    companion object {
        const val table = "MealIngredient"
        const val mealId = "meal_submission_id"
        const val ingredientId = "ingredient_submission_id"
        const val quantity = "quantity"
        const val attributes = "$table.$mealId, $table.$ingredientId, $table.$quantity"
    }

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "INNER JOIN $M_table " +
            "ON $M_table.$M_id = $table.$ingredientId " +
            "WHERE $M_table.$M_type = '$MEAL_TYPE_SUGGESTED_INGREDIENT' " +
            "AND $table.$mealId = :mealId"
    )
    fun getMealIngredientsByMealId(@Bind mealId: Int): Stream<DbMealIngredientDto>

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "INNER JOIN $M_table " +
            "ON $M_table.$M_id = $table.$ingredientId " +
            "WHERE $M_table.$M_type != '$MEAL_TYPE_SUGGESTED_INGREDIENT' " +
            "AND $table.$mealId = :mealId"
    )
    fun getMealComponentsByMealId(@Bind mealId: Int): Stream<DbMealIngredientDto>

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId, $quantity) values <mealIngredientParams> RETURNING *")
    fun insertAll(
            @BindBeanList(propertyNames = [mealId, ingredientId, quantity])
            mealIngredientParams: Collection<DbMealIngredientDto>
    ): Stream<DbMealIngredientDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submissionId RETURNING *")
    fun deleteAllByMealId(submissionId: Int): Stream<DbMealIngredientDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $mealId = :submissionId" +
            " AND $ingredientId in (<deleteIngredientIds>) RETURNING *")
    fun deleteAllByMealIdAndIngredientIds(
            @Bind submissionId: Int,
            @BindList deleteIngredientIds: Collection<Int>
    ): Stream<DbMealIngredientDto>
}