package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.clientError.DuplicateInsulinProfileException
import pt.isel.ps.g06.httpserver.common.exception.clientError.MissingInsulinProfileException
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import java.time.LocalTime

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
                    ?: throw MissingInsulinProfileException(profileName)
        }
    }

    /**
     * @throws DuplicateInsulinProfileException If an insulin profile with the name [profileName] already exists.
     */
    fun insertProfile(submitterId: Int,
                      profileName: String,
                      startTime: LocalTime,
                      endTime: LocalTime,
                      glucoseObjective: Int,
                      insulinSensitivityFactor: Int,
                      carbohydrateRatio: Int
    ): DbUserInsulinProfileDto {
        return jdbi.inTransaction<DbUserInsulinProfileDto, Exception>(isolationLevel) { handle ->

            val insulinProfileDao = handle.attach(insulinProfileDaoClass)

            insulinProfileDao.getFromUser(submitterId, profileName)?.also {
                throw DuplicateInsulinProfileException()
            }

            return@inTransaction insulinProfileDao.insertProfile(
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

    fun deleteProfile(submitterId: Int, profileName: String): DbUserInsulinProfileDto {
        return jdbi.inTransaction<DbUserInsulinProfileDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .deleteProfile(submitterId, profileName)
        } ?: throw MissingInsulinProfileException(profileName)
    }
}