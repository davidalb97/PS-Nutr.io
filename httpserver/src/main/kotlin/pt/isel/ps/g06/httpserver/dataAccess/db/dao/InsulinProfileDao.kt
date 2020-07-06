package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto

interface InsulinProfileDao {

    companion object {
        const val table = "InsulinProfile"
        const val submitterId = "submitter_id"
        const val profileName = "profile_name"
        const val startTime = "start_time"
        const val endTime = "end_time"
        const val glucoseObjective = "glucose_objective"
        const val insulinSensitivityFactor = "insulin_sensitivity_factor"
        const val carbohydrateRatio = "carbohydrate_ratio"
    }

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId")
    fun getAllFromUser(submitterId: Int): Collection<DbUserInsulinProfileDto>

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId AND $profileName = :profileName")
    fun getFromUser(submitterId: Int, profileName: String): DbUserInsulinProfileDto

    // TODO
    @SqlQuery("INSERT INTO $table($submitterId, $profileName, $startTime, $endTime, $glucoseObjective, $insulinSensitivityFactor, $carbohydrateRatio) VALUES(:submitterId, :profileName, :startTime, :endTime, :glucoseObjective, :insulinSensitivityFactor, :carbohydrateRatio) RETURNING *")
    fun insertProfile(submitterId: Int, profileName: String, startTime: String, endTime: String,
                      glucoseObjective: Int, insulinSensitivityFactor: Int, carbohydrateRatio: Int
    ): DbUserInsulinProfileDto

    @SqlQuery("DELETE FROM $table WHERE $submitterId = :submitterId AND $profileName = :profileName")
    fun deleteProfile(submitterId: Int, profileName: String): Boolean
}