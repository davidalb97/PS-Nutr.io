package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import java.util.stream.Stream

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

private const val MC_table = MealCuisineDao.table
private const val MC_mealId = MealCuisineDao.mealId
private const val MC_cuisineId = MealCuisineDao.cuisineId

private const val RC_table = RestaurantCuisineDao.table
private const val RC_restaurantId = RestaurantCuisineDao.id
private const val RC_cuisineId = RestaurantCuisineDao.cuisineId

interface CuisineDao {

    companion object {
        const val table = "Cuisine"
        const val name = "cuisine_name"
        const val id = "submission_id"
    }

    @SqlQuery("SELECT * FROM $table " +
            "ORDER BY $table.$name ASC " +
            "LIMIT :count " +
            "OFFSET :skip")
    fun getAll(skip: Int?, count: Int?): Stream<DbCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $name = :name")
    fun getByName(@Bind name: String): Stream<DbCuisineDto>

    @SqlQuery("SELECT $table.$id, $table.$name" +
            " FROM $table " +
            " INNER JOIN $MC_table " +
            " ON $MC_table.$MC_cuisineId = $table.$id" +
            " WHERE $MC_table.$MC_mealId = :mealId"
    )
    fun getByMealId(@Bind mealId: Int): Stream<DbCuisineDto>

    @SqlQuery("SELECT $table.$id, $table.$name" +
            " FROM $table " +
            " INNER JOIN $RC_table " +
            " ON $RC_table.$RC_cuisineId = $table.$id" +
            " WHERE $RC_table.$RC_restaurantId = :restaurantId"
    )
    fun getAllByRestaurantId(@Bind restaurantId: Int): Stream<DbCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $name in (<cuisineIds>)")
    fun getAllByNames(@BindList cuisineIds: Collection<String>): Stream<DbCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :cuisineId")
    fun getById(@Bind cuisineId: String): Stream<DbCuisineDto>

    @SqlQuery("SELECT $table.$id, $table.$name" +
            " FROM $table " +
            " INNER JOIN $AC_table " +
            " ON $table.$id = $AC_table.$AC_cuisineId" +
            " INNER JOIN $AS_table" +
            " ON $AS_table.$AS_submissionId = $AC_table.$AC_submissionId" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $AC_table.$AC_submissionId" +
            " WHERE $SS_table.$SS_submitterId = :submitterId" +
            " AND $AS_table.$AS_apiId IN (<apiIds>)"
    )
    fun getAllByApiSubmitterAndApiIds(
            @Bind submitterId: Int,
            @BindList apiIds: Collection<String>
    ): Stream<DbCuisineDto>
}