package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto

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

//ApiSubmission constants
private const val M_table = MealDao.table
private const val M_name = MealDao.name
private const val M_id = MealDao.id

interface MealCuisineDao {

    companion object {
        const val table = "MealCuisine"
        const val cuisineId = "cuisine_id"
        const val mealId = "meal_submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<MealCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getAllByMealId(@Bind mealId: Int): List<MealCuisineDto>

    @SqlQuery("SELECT $table.$mealId, $table.$cuisineId" +
            " FROM $table " +
            " INNER JOIN $M_table " +
            " ON $M_table.$M_id = $table.$mealId " +
            " INNER JOIN $C_table " +
            " ON $C_table.$C_cuisineId = $table.$cuisineId " +
            " WHERE $C_table.$C_name IN (<cuisineNames>)")
    fun getByCuisineNames(
            @BindList("cuisineNames") cuisineNames: Collection<String>
    ): Collection<MealCuisineDto>

    @SqlQuery("INSERT INTO $table($mealId, $cuisineId)" +
            " VALUES(:submissionId, :cuisineName) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind cuisineId: Int): MealCuisineDto

    @SqlQuery("INSERT INTO $table($mealId, $cuisineId) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [mealId, cuisineId]
    ) newName: List<MealCuisineDto>): List<MealCuisineDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submissionId RETURNING *")
    fun deleteAllByMealId(@Bind submissionId: Int): List<MealCuisineDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $mealId = :submissionId" +
            " AND $cuisineId in <cuisineIds> RETURNING *")
    fun deleteAllByMealIdAndCuisineIds(
            @Bind submissionId: Int,
            @BindList("cuisineIds") cuisineIds: Collection<Int>
    ): Collection<MealCuisineDto>
}