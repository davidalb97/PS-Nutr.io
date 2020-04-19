package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbMealIngredient

private const val table = "MealIngredient"
private const val mealId = "meal_submission_id"
private const val ingredientId = "ingredient_submission_id"

interface MealIngredientDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbMealIngredient>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getByMealId(@Bind mealId: Int): DbMealIngredient

    @SqlQuery("SELECT * FROM $table WHERE $ingredientId = :ingredientId")
    fun getByIngredientId(@Bind ingredientId: Int): DbMealIngredient

    @SqlQuery("INSERT INTO $table($mealId, $ingredientId) VALUES(:mealId, :ingredientId)")
    fun insert(@Bind mealId: Int, @Bind ingredientId: Int)
}