package pt.isel.ps.g06.httpserver.dataAccess.database.daos

import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import pt.isel.ps.g06.httpserver.dataAccess.database.model.DbMeal
import pt.isel.ps.g06.httpserver.dataAccess.database.model.DbRestaurant

interface MealDao {

    @SqlQuery("SELECT * FROM Meal WHERE meal_name = :mealName")
    fun getMealsByName(@Bind mealName: String): List<DbMeal>

    @SqlQuery("INSERT INTO Meal(meal_name) VALUES(:mealName)")
    @GetGeneratedKeys
    fun insertMeal(@Bind mealName: String): Int

    @SqlQuery("SELECT * FROM Meal WHERE meal_name = :mealName")
    fun getGenericMealsByName(@Bind mealName: String): List<DbMeal>
}