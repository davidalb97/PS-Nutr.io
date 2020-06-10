package pt.isel.ps.g06.httpserver.dataAccess.db.dao.info

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantInfoDto

//Restaurant table constants
private const val R_table = RestaurantDao.table
private const val R_id = RestaurantDao.id
private const val R_name = RestaurantDao.name
private const val R_latitude = RestaurantDao.latitude
private const val R_longitude = RestaurantDao.longitude

//Votable table constants
private const val V_table = VotableDao.table
private const val V_id = VotableDao.id
private const val V_positive = VotableDao.positiveCount
private const val V_negative = VotableDao.negativeCount

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

//ApiSubmission constants
private const val AS_table = ApiSubmissionDao.table
private const val AS_submissionId = ApiSubmissionDao.submissionId
private const val AS_apiId = ApiSubmissionDao.apiId

//ApiSubmission constants
private const val UV_table = UserVoteDao.table
private const val UV_submissionId = UserVoteDao.submissionId
private const val UV_voterSubmitterId = UserVoteDao.voterSubmitterId
private const val UV_vote = UserVoteDao.vote

private const val colsFromTable = " $SS_table.$SS_submitterId, $SS_table.$SS_submissionId," +
        " $AS_table.$AS_apiId," +
        " $R_table.$R_name, $R_table.$R_latitude, $R_table.$R_longitude," +
        " $V_table.$V_positive, $V_table.$V_negative," +
        " $UV_table.$UV_vote" +
        " FROM $R_table" +
        " INNER JOIN $V_table" +
        " ON $R_table.$R_id = $V_table.$V_id" +
        " INNER JOIN $SS_table" +
        " ON $SS_table.$SS_submissionId = $R_table.$R_id" +
        " FULL OUTER JOIN $UV_table" +
        " ON $UV_table.$UV_submissionId = $R_table.$R_id AND $UV_table.$UV_voterSubmitterId = :userId" +
        " FULL OUTER JOIN $AS_table" +
        " ON $AS_table.$AS_submissionId = $R_table.$R_id"

interface RestaurantInfoDao {

    @SqlQuery("SELECT $colsFromTable" +
            " WHERE " +
            "ST_Distance(" +
            "ST_MakePoint($R_table.$R_latitude, $R_table.$R_longitude)::geography," +
            "ST_MakePoint(:latitude, :longitude)::geography, " +
            "false" +
            ") <= :radius"
    )
    fun getByCoordinates(@Bind latitude: Float, @Bind longitude: Float, @Bind radius: Int, @Bind userId: Int): Collection<DbRestaurantInfoDto>

    @SqlQuery("SELECT $colsFromTable" +
            " INNER JOIN $V_table" +
            " ON $R_table.$R_id = $V_table.$V_id" +
            " WHERE $R_id = :submissionId"
    )
    fun getById(@Bind submissionId: Int, @Bind userId: Int): DbRestaurantInfoDto?
}