package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import java.util.stream.Stream

private val insulinProfileDaoClass = InsulinProfileDao::class.java

@Repository
class InsulinProfileDbRepository(private val databaseContext: DatabaseContext) {

    fun getAllFromUser(submitterId: Int): Stream<DbUserInsulinProfileDto> {
        return databaseContext.inTransaction {
            it.attach(insulinProfileDaoClass).getAllFromUser(submitterId)
        }
    }

    fun getFromUser(submitterId: Int, profileName: String): DbUserInsulinProfileDto? {
        return databaseContext.inTransaction {
            it.attach(insulinProfileDaoClass).getFromUser(submitterId, profileName)
        }
    }

    fun insertProfile(
            submitterId: Int,
            profileName: String,
            startTime: String,
            endTime: String,
            glucoseObjective: Int,
            insulinSensitivityFactor: Int,
            carbohydrateRatio: Int
    ): DbUserInsulinProfileDto {
        return databaseContext.inTransaction {
            it.attach(insulinProfileDaoClass).insertProfile(
                    submitterId = submitterId,
                    profileName = profileName,
                    startTime = startTime,
                    endTime = endTime,
                    glucoseObjective = glucoseObjective,
                    sensitivityFactor = insulinSensitivityFactor,
                    carbRatio = carbohydrateRatio
            )
        }
    }

    fun deleteProfile(submitterId: Int, profileName: String) {
        return databaseContext.inTransaction {
            it.attach(insulinProfileDaoClass).deleteProfile(submitterId, profileName)
        }
    }
}