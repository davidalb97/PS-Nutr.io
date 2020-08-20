package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

//ApiSubmission constants
private const val AS_table = ApiSubmissionDao.table
private const val AS_submissionId = ApiSubmissionDao.submissionId
private const val AS_apiId = ApiSubmissionDao.apiId

interface RestaurantDao {

    companion object {
        const val table = "Restaurant"
        const val id = "submission_id"
        const val name = "restaurant_name"
        const val latitude = "latitude"
        const val longitude = "longitude"
        const val ownerId = "owner_id"
    }

    @SqlQuery("SELECT * FROM $table WHERE " +
            "ST_Distance(" +
            "ST_MakePoint($table.$latitude, $table.$longitude)::geography," +
            "ST_MakePoint(:latitude, :longitude)::geography, " +
            "false" +
            ") <= :radius"
    )
    fun getByCoordinates(@Bind latitude: Float, @Bind longitude: Float, @Bind radius: Int): Collection<DbRestaurantDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getBySubmissionId(@Bind submissionId: Int): DbRestaurantDto?

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$latitude, $table.$longitude, $table.$ownerId " +
            "FROM $table " +
            "INNER JOIN $SS_table " +
            "ON $SS_table.$SS_submissionId = $table.$id " +
            "INNER JOIN $AS_table " +
            "ON $AS_table.$AS_submissionId = $table.$id " +
            "WHERE $SS_table.$SS_submitterId = :apiSubmitterId " +
            "AND $AS_table.$AS_apiId = :apiId"
    )
    fun getApiRestaurant(@Bind apiSubmitterId: Int, @Bind apiId: String): DbRestaurantDto?


    @SqlQuery("INSERT INTO $table($id, $name, $latitude, $longitude, $ownerId)" +
            " VALUES(:submissionId, :restaurantName, :latitude, :longitude, :ownerId) RETURNING *")
    fun insert(@Bind submissionId: Int,
               @Bind restaurantName: String,
               @Bind latitude: Float,
               @Bind longitude: Float,
               @Bind ownerId: Int?
    ): DbRestaurantDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): DbRestaurantDto

    @SqlQuery("UPDATE $table SET $name = :name WHERE $id = :submissionId RETURNING *")
    fun update(submissionId: Int, name: String): Collection<DbRestaurantDto>
}