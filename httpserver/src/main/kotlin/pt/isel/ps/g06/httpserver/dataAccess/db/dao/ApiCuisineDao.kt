package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiCuisineDto

interface ApiCuisineDao {

    companion object {
        const val table = "ApiCuisine"
        const val submissionId = "submission_id"
        const val cuisineId = "cuisine_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbApiCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getBySubmissionId(@Bind submissionId: Int): DbApiCuisineDto

    @SqlQuery("SELECT * FROM $table WHERE $cuisineId = :cuisineId")
    fun getAllByCuisineId(@Bind cuisineId: Int): List<DbApiCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $cuisineId = :cuisineId")
    fun getAllByCuisineName(@Bind cuisineId: Int): List<DbApiCuisineDto>

    @SqlQuery("INSERT INTO $table($submissionId, $cuisineId)" +
            " VALUES(:submissionId, :cuisineId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind cuisineId: Int): DbApiCuisineDto

    @SqlQuery("INSERT INTO $table($submissionId, $cuisineId)" +
            " values <apiCuisines> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [submissionId, cuisineId])
                  apiCuisines: List<DbApiCuisineDto>
    ): List<DbApiCuisineDto>
}