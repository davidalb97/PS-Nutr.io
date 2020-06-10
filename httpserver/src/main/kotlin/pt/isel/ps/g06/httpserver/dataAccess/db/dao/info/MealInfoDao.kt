package pt.isel.ps.g06.httpserver.dataAccess.db.dao.info

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbMealInfoDto

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

interface MealInfoDao {

    companion object {
        const val M_table = MealDao.table
        const val M_name = MealDao.name
        const val M_id = MealDao.id
        const val V_table = VotableDao.table
        const val V_id = VotableDao.id
        const val V_positive = VotableDao.positiveCount
        const val V_negative = VotableDao.negativeCount
        private const val colsFromTable = "$M_table.$M_id, $M_table.$M_name, $V_table.$V_positive, $V_table.$V_negative" +
                " FROM $M_table" +
                " INNER JOIN $V_table" +
                " ON $M_table.$M_id = $V_table.$V_id"
    }

    @SqlQuery("SELECT $colsFromTable")
    fun getAll(): List<DbMealInfoDto>

    @SqlQuery("SELECT $colsFromTable WHERE $M_table.$M_id = :submissionId")
    fun getById(@Bind submissionId: Int): DbMealInfoDto?

    @SqlQuery("SELECT $colsFromTable WHERE $M_table.$M_name = :mealName")
    fun getByName(@Bind mealName: String): DbMealInfoDto?

    @SqlQuery("SELECT DISTINCT ($M_table.$M_id) $colsFromTable" +
            " INNER JOIN $MC_table" +
            " ON $M_table.$M_id = $MC_table.$MC_mealId" +
            " INNER JOIN $C_table" +
            " ON $MC_table.$MC_cuisineId = $C_table.$C_cuisineId" +
            " WHERE $C_table.$C_name IN (<mealNames>)"
    )
    fun getAllByCuisineNames(@BindList mealNames: Collection<String>): Collection<DbMealInfoDto>

    @SqlQuery("SELECT $$colsFromTable" +
            " INNER JOIN $AC_table " +
            " ON $C_table.$C_cuisineId = $AC_table.$AC_cuisineId" +
            " INNER JOIN $AS_table" +
            " ON $AS_table.$AS_submissionId = $AC_table.$AC_submissionId" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $AC_table.$AC_submissionId" +
            " INNER JOIN $MC_table" +
            " ON $MC_table.$MC_cuisineId = $C_table.$C_cuisineId" +
            " INNER JOIN $M_table" +
            " ON $M_table.$M_id = $MC_table.$MC_mealId" +
            " WHERE $SS_table.$SS_submitterId = :submitterId" +
            " AND $AS_table.$AS_apiId IN (<apiIds>)"
    )
    fun getAllByApiSubmitterAndCuisineApiIds(
            @Bind submitterId: Int,
            @BindList apiIds: Collection<String>
    ): Collection<DbMealInfoDto>
}