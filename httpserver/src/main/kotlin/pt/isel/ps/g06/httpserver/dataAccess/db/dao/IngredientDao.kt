package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbIngredientDto

//SubmissionSubmitter constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

//ApiSubmission constants
private const val AS_table = ApiSubmissionDao.table
private const val AS_submissionId = ApiSubmissionDao.submissionId
private const val AS_apiId = ApiSubmissionDao.apiId

interface IngredientDao {

    companion object {
        const val table = "Ingredient"
        const val id = "submission_id"
        const val name = "ingredient_name"
        const val carbs = "carbs"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbIngredientDto>

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$carbs" +
            " FROM $table" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $table.$id" /*+
            " INNER JOIN $AS_table" +
            " ON $AS_table.$AS_submissionId = $table.$id" +
            " WHERE $SS_table.$SS_submitterId = :submitterId"*/
    )

    fun getAllBySubmitterId(submitterId: Int): List<DbIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $name = :ingredientName")
    fun getAllByName(@Bind ingredientName: String): List<DbIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $id in (<submissionIds>)")
    fun getAllByIds(@BindList submissionIds: List<Int>): List<DbIngredientDto>

    @SqlQuery("SELECT * FROM $table WHERE $carbs = :carbs")
    fun getAllByCarbs(@Bind carbs: Int) : List<DbIngredientDto>

    @SqlQuery("INSERT INTO $table($id, $name, $carbs) values <ingredientParams> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [id, name, carbs])
                  ingredientParams: List<IngredientParam>
    ): List<DbIngredientDto>
}

//Variable names must match sql columns!!!
data class IngredientParam(
        val submission_id: Int,
        val ingredient_name: String,
        val carbs: Int
)