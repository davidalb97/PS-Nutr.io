package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiCuisineDto
import java.util.stream.Stream

interface ApiCuisineDao {

    companion object {
        const val table = "ApiCuisine"
        const val submissionId = "submission_id"
        const val cuisineId = "cuisine_submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): Stream<DbApiCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $cuisineId = :cuisineId")
    fun getAllByCuisineId(@Bind cuisineId: Int): Stream<DbApiCuisineDto>
}