package pt.isel.ps.g06.httpserver.dataAccess.db.dao.info

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VotableDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantInfoDto



interface RestaurantInfoDao {

    companion object {
        const val R_table = RestaurantDao.table
        const val R_id = RestaurantDao.id
        const val R_name = RestaurantDao.name
        const val R_latitude = RestaurantDao.latitude
        const val R_longitude = RestaurantDao.longitude
        const val V_table = VotableDao.table
        const val V_id = VotableDao.id
        const val V_positive = VotableDao.positiveCount
        const val V_negative = VotableDao.negativeCount
    }

    @SqlQuery("SELECT $R_table.$R_id, $R_table.$R_name, $R_table.$R_latitude, $R_table.$R_longitude," +
            " $V_table.$V_positive, $V_table.$V_negative" +
            " FROM $R_table" +
            " INNER JOIN $V_table" +
            " ON $R_table.$R_id = $V_table.$V_id" +
            " WHERE " +
            "ST_Distance(" +
            "ST_MakePoint($R_table.$R_latitude, $R_table.$R_longitude)::geography," +
            "ST_MakePoint(:latitude, :longitude)::geography, " +
            "false" +
            ") <= :radius"
    )
    fun getByCoordinates(@Bind latitude: Float, @Bind longitude: Float, @Bind radius: Int): Collection<DbRestaurantInfoDto>

    @SqlQuery("SELECT $R_table.$R_id, $R_table.$R_name, $R_table.$R_latitude, $R_table.$R_longitude," +
            " $V_table.$V_positive, $V_table.$V_negative" +
            " FROM $R_table" +
            " INNER JOIN $V_table" +
            " ON $R_table.$R_id = $V_table.$V_id" +
            " WHERE $R_id = :submissionId"
    )
    fun getById(@Bind submissionId: Int): DbRestaurantInfoDto?
}