package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val insulinProfileDaoClass = InsulinProfileDao::class.java

@Repository
class InsulinProfileDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAllFromUser(submitterId: Int): Sequence<DbUserInsulinProfileDto> {
        val userInsulinProfiles = lazy {
            jdbi.inTransaction<Collection<DbUserInsulinProfileDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(insulinProfileDaoClass)
                        .getAllFromUser(submitterId)

            }
        }

        return Sequence { userInsulinProfiles.value.iterator() }
    }

    fun getFromUser(submitterId: Int, profileName: String): DbUserInsulinProfileDto {
        return jdbi.inTransaction<DbUserInsulinProfileDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .getFromUser(submitterId, profileName)

        }
    }

    fun insertProfile(dto: DbUserInsulinProfileDto): DbUserInsulinProfileDto {
        return jdbi.inTransaction<DbUserInsulinProfileDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .insertProfile(
                            submitterId = dto.submitterId,
                            profileName = dto.profileName,
                            startTime = dto.startTime,
                            endTime = dto.endTime,
                            glucoseObjective = dto.glucoseObjective,
                            insulinSensitivityFactor = dto.insulinSensitivityFactor,
                            carbohydrateRatio = dto.carbohydrateRatio
                    )
        }
    }

    fun deleteProfile(submitterId: Int, profileName: String): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .deleteProfile(submitterId, profileName)
        }
    }
}