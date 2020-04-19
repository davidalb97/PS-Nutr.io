package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbMeal

private const val mealTable = "Meal"
private const val mealName = "meal_name"

interface MealDao {

    @SqlQuery("SELECT * FROM $mealTable WHERE $mealName = :mealName")
    fun getMealsByName(@Bind mealName: String): List<DbMeal>

    @SqlQuery("INSERT INTO $mealTable($mealName) VALUES(:mealName)")
    @GetGeneratedKeys
    fun insertMeal(@Bind mealName: String): Int

    @SqlQuery("SELECT * FROM $mealTable WHERE $mealName = :mealName")
    fun getGenericMealsByName(@Bind mealName: String): List<DbMeal>
}