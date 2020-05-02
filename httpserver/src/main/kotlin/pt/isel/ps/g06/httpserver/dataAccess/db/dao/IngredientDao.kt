package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.IngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient

private const val table = "Ingredient"
private const val name = "ingredient_name"
private const val id = "submission_id"

private const val SS_table = "SubmissionSubmitter"
private const val SS_submissionId = "submission_id"
private const val SS_submitterId = "submitter_id"

private const val AS_table = "ApiSubmission"
private const val AS_submissionId = "submission_id"
private const val AS_apiId = "apiId"

interface IngredientDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<IngredientDto>

    @SqlQuery("SELECT $AS_table.$AS_submissionId" +
            " FROM $table" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $table.$id" +
            " INNER JOIN $AS_table" +
            " ON $AS_table.$AS_submissionId = $table.$id" +
            " WHERE $SS_table.$SS_submitterId = :submitterId"
    )
    fun getAllSubmissionIdsBySubmitterId(submitterId: Int): List<Ingredient>

    @SqlQuery("SELECT * FROM $table WHERE $name = :ingredientName")
    fun getByName(@Bind ingredientName: String): IngredientDto?

    @SqlQuery("INSERT INTO $table($id, $name) VALUES(:submissionId, :ingredientName) RETURNING *")
    fun insert(@Bind submissionId: Int, ingredientName: String): IngredientDto

    @SqlQuery("INSERT INTO $table($id, $name) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [id, name]
    ) values: List<IngredientParam>): List<IngredientDto>
}

//Variable names must match sql columns!!!
data class IngredientParam(val submission_id: Int, val ingredient_name: String)