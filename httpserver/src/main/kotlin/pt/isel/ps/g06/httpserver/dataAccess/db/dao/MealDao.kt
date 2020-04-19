package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbMeal

private const val table = "Meal"
private const val name = "meal_name"
private const val id = "submission_id"

interface MealDao {

    @SqlQuery("SELECT * FROM $table WHERE $id = :submission_id")
    fun getById(@Bind submission_id: Int): List<DbMeal>

    @SqlQuery("SELECT * FROM $table WHERE $name = :mealName")
    fun getByName(@Bind mealName: String): List<DbMeal>

    @SqlQuery("INSERT INTO $table($id, $name) VALUES(:submission_id, :mealName)")
    fun insert(@Bind submission_id: Int, @Bind mealName: String): Int

}