package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto

interface RestaurantDao {

    companion object {
        const val table = "Restaurant"
        const val id = "submission_id"
        const val name = "restaurant_name"
        const val latitude = "latitude"
        const val longitude = "longitude"
    }

    @SqlQuery("SELECT * FROM $table WHERE " +
            "ST_Distance(" +
            "ST_MakePoint($table.latitude, $table.longitude)::geography," +
            "ST_MakePoint(:latitude, :longitude)::geography, " +
            "false" +
            ") <= :radius"
    )
    fun getByCoordinates(@Bind latitude: Float, @Bind longitude: Float, @Bind radius: Int): List<DbRestaurantDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): DbRestaurantDto?

    @SqlQuery("INSERT INTO $table($id, $name, $latitude, $longitude)" +
            " VALUES(:submissionId, :restaurantName, :latitude, :longitude) RETURNING *")
    fun insert(@Bind submissionId: Int,
               @Bind restaurantName: String,
               @Bind latitude: Float,
               @Bind longitude: Float
    ): DbRestaurantDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): DbRestaurantDto

    @SqlQuery("UPDATE $table SET $name = :name WHERE $id = :submissionId RETURNING *")
    fun update(submissionId: Int, name: String): Collection<DbRestaurantDto>
}