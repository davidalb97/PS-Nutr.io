package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto

//Cuisine table constants
private const val C_table = CuisineDao.table
private const val C_name = CuisineDao.name
private const val C_cuisineId = CuisineDao.id

//ApiCuisine table constants
private const val AC_table = ApiCuisineDao.table
private const val AC_submissionId = ApiCuisineDao.submissionId
private const val AC_cuisineId = ApiCuisineDao.cuisineId

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

//ApiSubmission constants
private const val AS_table = ApiSubmissionDao.table
private const val AS_submissionId = ApiSubmissionDao.submissionId
private const val AS_apiId = ApiSubmissionDao.apiId

//MealCuisine constants
private const val MC_table = MealCuisineDao.table
private const val MC_mealId = MealCuisineDao.mealId
private const val MC_cuisineId = MealCuisineDao.cuisineId

interface MealDao {

    companion object {
        const val table = "Meal"
        const val name = "meal_name"
        const val id = "submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbMealDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): DbMealDto?

    @SqlQuery("SELECT * FROM $table WHERE $name = :mealName")
    fun getByName(@Bind mealName: String): List<DbMealDto>

    @SqlQuery("SELECT DISTINCT $table.$id, $table.$name" +
            " FROM $table" +
            " INNER JOIN $MC_table" +
            " ON $table.$id = $MC_table.$MC_mealId" +
            " INNER JOIN $C_table" +
            " ON $MC_table.$MC_cuisineId = $C_table.$C_cuisineId" +
            " WHERE $C_table.$C_name IN (<mealNames>)"
    )
    fun getAllByCuisineNames(@BindList mealNames: Collection<String>): Collection<DbMealDto>

    @SqlQuery("SELECT $table.$id, $table.$name" +
            " FROM $C_table" +
            " INNER JOIN $AC_table " +
            " ON $C_table.$C_cuisineId = $AC_table.$AC_cuisineId" +
            " INNER JOIN $AS_table" +
            " ON $AS_table.$AS_submissionId = $AC_table.$AC_submissionId" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $AC_table.$AC_submissionId" +
            " INNER JOIN $MC_table" +
            " ON $MC_table.$MC_cuisineId = $C_table.$C_cuisineId" +
            " INNER JOIN $table" +
            " ON $table.$id = $MC_table.$MC_mealId" +
            " WHERE $SS_table.$SS_submitterId = :submitterId" +
            " AND $AS_table.$AS_apiId IN (<apiIds>)"
    )
    fun getAllByApiSubmitterAndCuisineApiIds(
            @Bind submitterId: Int,
            @BindList apiIds: Collection<String>
    ): Collection<DbMealDto>

    @SqlQuery("INSERT INTO $table($id, $name) VALUES(:submissionId, :mealName) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind mealName: String): DbMealDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): DbMealDto

    @SqlQuery("UPDATE $table SET $name = :new_name WHERE $id = :submissionId RETURNING *")
    fun update(@Bind submissionId: Int, new_name: String): DbMealDto
}