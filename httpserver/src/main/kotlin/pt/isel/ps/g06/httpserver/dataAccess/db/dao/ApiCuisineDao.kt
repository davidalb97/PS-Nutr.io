package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiCuisineDto

interface ApiCuisineDao {

    companion object {
        const val table = "ApiCuisine"
        const val submissionId = "submission_id"
        const val cuisineId = "cuisine_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<ApiCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getBySubmissionId(@Bind submissionId: Int): ApiCuisineDto

    @SqlQuery("SELECT * FROM $table WHERE $cuisineId = :cuisineId")
    fun getAllByCuisineId(@Bind cuisineId: Int): List<ApiCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $cuisineId = :cuisineId")
    fun getAllByCuisineName(@Bind cuisineId: Int): List<ApiCuisineDto>

    @SqlQuery("INSERT INTO $table($submissionId, $cuisineId)" +
            " VALUES(:submissionId, :cuisineId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind cuisineId: Int): ApiCuisineDto

    @SqlQuery("INSERT INTO $table($submissionId, $cuisineId)" +
            " values <apiCuisines> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [submissionId, cuisineId])
                  apiCuisines: List<ApiCuisineDto>
    ): List<ApiCuisineDto>
}