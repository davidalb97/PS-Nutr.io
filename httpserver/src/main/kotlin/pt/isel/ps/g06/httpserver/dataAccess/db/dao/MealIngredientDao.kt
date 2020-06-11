package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealIngredientDto

interface MealIngredientDao {

    companion object {
        const val table = "MealIngredient"
        const val mealId = "meal_submission_id"
        const val ingredientId = "ingredient_submission_id"
        const val quantity = "quantity"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbMealIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getAllByMealId(@Bind mealId: Int): List<DbMealIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $ingredientId = :ingredientId")
    fun getAllByIngredientId(@Bind ingredientId: Int): List<DbMealIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $quantity = :mealAmount")
    fun getAllByAmount(@Bind mealAmount: Int): List<DbMealIngredientDto>

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