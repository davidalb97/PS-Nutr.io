package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantDto

private const val table = "Restaurant"
private const val id = "submission_id"
private const val name = "restaurant_name"
private const val latitude = "latitude"
private const val longitude = "longitude"

interface RestaurantDao {

    @SqlQuery("SELECT * FROM $table WHERE " +
            "ST_Distance(" +
            "ST_MakePoint($table.latitude, $table.longitude)::geography," +
            "ST_MakePoint(:latitude, :longitude)::geography, " +
            "false" +
            ") <= :radius"
    )
    fun getByCoordinates(@Bind latitude: Float, @Bind longitude: Float, @Bind radius: Int): List<RestaurantDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    @Transaction(TransactionIsolationLevel.SERIALIZABLE)
    fun getById(@Bind submissionId: Int): RestaurantDto?

    @SqlQuery("INSERT INTO $table($id, $name, $latitude, $longitude)" +
            " VALUES(:submissionId, :restaurantName, :latitude, :longitude) RETURNING *")
    fun insert(@Bind submissionId: Int,
               @Bind restaurantName: String,
               @Bind latitude: Float,
               @Bind longitude: Float
    ): RestaurantDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): RestaurantDto

    @SqlQuery("UPDATE $table SET $name = :name WHERE $id = :submissionId RETURNING *")
    fun update(submissionId: Int, name: String)
}