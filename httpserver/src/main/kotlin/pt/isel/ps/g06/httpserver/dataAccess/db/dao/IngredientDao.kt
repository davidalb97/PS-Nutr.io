package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbIngredient

private const val table = "Ingredient"
private const val name = "ingredient_name"
private const val id = "submission_id"

interface IngredientDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbIngredient>

    @SqlQuery("SELECT * FROM $table WHERE $name = :ingredientName")
    fun getByName(@Bind ingredientName: String): DbIngredient

    @SqlQuery("INSERT INTO $table($id, $name) VALUES(:submissionId, :ingredientName)")
    fun insert(@Bind submissionId: Int, ingredientName: String): Int
}