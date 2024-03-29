package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportSubmissionDetailDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportedSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionReportDto

//Submission constants
private const val S_table = SubmissionDao.table
private const val S_submission_id = SubmissionDao.id
private const val S_submission_type = SubmissionDao.type

//Submission constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

//Restaurant constants
private const val R_table = RestaurantDao.table
private const val R_submission_id = RestaurantDao.id
private const val R_restaurant_name = RestaurantDao.name

//RestaurantMeal constants
private const val RM_table = RestaurantMealDao.table
private const val RM_submission_id = RestaurantMealDao.id
private const val RM_restaurant_meal_id = RestaurantMealDao.mealId

//Meal constants
private const val M_table = MealDao.table
private const val M_submission_id = MealDao.id
private const val M_meal_name = MealDao.name

interface ReportDao {

    companion object {
        const val table = "Report"
        const val reportId = "report_id"
        const val reporterId = "submitter_id"
        const val submissionId = "submission_id"
        const val description = "description"
    }

    @SqlQuery("SELECT * FROM $table LIMIT :count OFFSET :skip * :count")
    fun getAll(skip: Int?, count: Int?): ResultIterable<DbReportDto>

    @SqlQuery( "SELECT $S_table.$S_submission_id as _submission_id, " +
                    "COALESCE($R_table.$R_restaurant_name, _RestaurantMeal.meal_name) as _name, " +
                    "$SS_table.$SS_submitterId as _submitter_id, " +
                    "COUNT($S_table.$S_submission_id) as _count FROM $table " +
                    "INNER JOIN $S_table ON $S_table.$S_submission_id = $table.$submissionId " +
                    "FULL OUTER JOIN $R_table ON $R_table.$R_submission_id = $table.$submissionId " +
                    "FULL OUTER JOIN " +
                    "(SELECT $RM_table.$RM_submission_id, $M_table.$M_meal_name " +
                    "FROM $RM_table INNER JOIN $M_table ON " +
                    "$M_table.$M_submission_id = $RM_table.$RM_restaurant_meal_id) as _RestaurantMeal " +
                    "ON _RestaurantMeal.submission_id = $S_table.$S_submission_id " +
                    "INNER JOIN $SS_table ON $SS_table.$SS_submissionId = $S_table.$S_submission_id " +
                    "WHERE $S_table.$S_submission_type IN (:submissionType) " +
                    "GROUP BY $S_table.$S_submission_id, $R_table.$R_restaurant_name, _RestaurantMeal.meal_name, _submitter_id " +
                    "ORDER BY _count DESC, _name ASC " +
                    "LIMIT :count OFFSET :skip * :count"
    )
    fun getAllReportedSubmissionsByType(
            submissionType: String,
            skip: Int?,
            count: Int?
    ): ResultIterable<DbReportedSubmissionDto>

    @SqlQuery("SELECT $table.$reportId, $table.$reporterId, $table.$description " +
            "FROM $table WHERE $table.$submissionId = :submissionId")
    fun getAllBySubmission(submissionId: Int): ResultIterable<DbSubmissionReportDto>

    @SqlQuery("SELECT DISTINCT COALESCE($R_table.$R_restaurant_name, _RestaurantMeal.meal_name) " +
            "AS _submission_name, $SS_table.$SS_submitterId FROM $table " +
            "INNER JOIN $S_table ON $S_table.$S_submission_id = $table.$submissionId " +
            "FULL OUTER JOIN $R_table ON $R_table.$R_submission_id = $table.$submissionId " +
            "FULL OUTER JOIN (SELECT $RM_table.$RM_submission_id, $M_table.$M_meal_name FROM $RM_table " +
            "INNER JOIN $M_table ON $M_table.$M_submission_id = $RM_table.$RM_restaurant_meal_id) as _RestaurantMeal " +
            "ON _RestaurantMeal.submission_id = $S_table.$S_submission_id " +
            "INNER JOIN $SS_table ON $SS_table.$SS_submissionId = $S_table.$S_submission_id " +
            "WHERE $S_table.$S_submission_id = :submissionId"
    )
    fun getReportedSubmissionDetail(submissionId: Int): DbReportSubmissionDetailDto?

    @SqlQuery("SELECT * FROM $table WHERE $reporterId = :submitterId AND $submissionId = :submissionId")
    fun getReportFromSubmitter(submitterId: Int, submissionId: Int): DbReportDto?

    @SqlQuery("INSERT INTO $table($reporterId, $submissionId, $description) " +
            "VALUES(:reporterSubmitterId, :submissionId, :description) RETURNING *")
    fun insert(@Bind reporterSubmitterId: Int, submissionId: Int, description: String): DbReportDto

    @SqlQuery("DELETE FROM $table WHERE $reportId = :reportId RETURNING *")
    fun deleteById(reportId: Int): DbReportDto
}