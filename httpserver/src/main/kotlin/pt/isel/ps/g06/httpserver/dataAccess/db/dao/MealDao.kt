package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbMeal

interface MealDao {

    @SqlQuery("SELECT * FROM Meal WHERE meal_name = :mealName")
    fun getMealsByName(@Bind mealName: String): List<DbMeal>

    @SqlQuery("INSERT INTO Meal(meal_name) VALUES(:mealName)")
    @GetGeneratedKeys
    fun insertMeal(@Bind mealName: String): Int

    @SqlQuery("SELECT * FROM Meal WHERE meal_name = :mealName")
    fun getGenericMealsByName(@Bind mealName: String): List<DbMeal>
}