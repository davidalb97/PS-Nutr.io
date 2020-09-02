package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserEncInsulinProfileDto
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbRowInsulinProfileMapper

@RegisterRowMapper(DbRowInsulinProfileMapper::class)
interface InsulinProfileDao {

    companion object {
        const val modificationDate = "modification_date"
        const val table = "InsulinProfile"
        const val submitterId = "submitter_id"
        const val profileName = "profile_name"
        const val startTime = "start_time"
        const val endTime = "end_time"
        const val glucoseObjective = "glucose_objective"
        const val sensitivityFactor = "insulin_sensitivity_factor"
        const val carbRatio = "carbohydrate_ratio"
    }

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId LIMIT :count OFFSET :skip")
    fun getAllFromUser(submitterId: Int, count: Int?, skip: Int?): ResultIterable<DbUserEncInsulinProfileDto>

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId AND $profileName = :profileName")
    fun getFromUser(submitterId: Int, profileName: String): DbUserEncInsulinProfileDto?

    @SqlQuery("INSERT INTO $table(" +
            "$submitterId, $profileName, $startTime, $endTime, $glucoseObjective, $sensitivityFactor, $carbRatio, $modificationDate" +
            ") VALUES(" +
            ":submitterId, :profileName, :startTime, :endTime, :glucoseObjective, :sensitivityFactor, :carbRatio, :modificationDate" +
            ") RETURNING *"
    )
    fun insertProfile(
            submitterId: Int, profileName: String, startTime: String, endTime: String,
            glucoseObjective: String, sensitivityFactor: String, carbRatio: String, modificationDate: String
    ): DbUserEncInsulinProfileDto

    @SqlQuery("DELETE FROM $table WHERE $submitterId = :submitterId RETURNING *")
    fun deleteAllBySubmitterId(submitterId: Int): ResultIterable<DbUserEncInsulinProfileDto>

    @SqlQuery("DELETE FROM $table WHERE $submitterId = :submitterId AND $profileName = :profileName RETURNING *")
    fun deleteProfile(submitterId: Int, profileName: String): DbUserEncInsulinProfileDto?
}