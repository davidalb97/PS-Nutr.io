package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto

interface InsulinProfileDao {

    companion object {
        const val table = "InsulinProfile"
        const val submitterId = "submitter_id"
        const val profileName = "profile_name"
    }

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId")
    fun getAllFromUser(submitterId: Int): Collection<DbUserInsulinProfileDto>

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitterId AND $profileName = :profileName")
    fun getFromUser(submitterId: Int, profileName: String): DbUserInsulinProfileDto

    //TODO
    fun insertProfile(): DbUserInsulinProfileDto

    //TODO
    fun deleteProfile(): Boolean
}