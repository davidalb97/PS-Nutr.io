package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbIngredient

private const val ingredientTable = "Ingredient"
private const val ingredientName = "ingredient_name"

interface IngredientDao {
    @SqlQuery("SELECT * FROM $ingredientTable")
    fun getIngredients(): List<DbIngredient>

    @SqlQuery("SELECT * FROM $ingredientTable WHERE $ingredientName = :ingredientName")
    fun getIngredientByName(@Bind ingredientName: String): DbIngredient

    @SqlQuery("INSERT INTO $ingredientTable(submission_id, $ingredientName) VALUES(:submissionId, :ingredientName)")
    @GetGeneratedKeys
    fun insertIngredient(@Bind submissionId: Int, ingredientName: String): Int
}