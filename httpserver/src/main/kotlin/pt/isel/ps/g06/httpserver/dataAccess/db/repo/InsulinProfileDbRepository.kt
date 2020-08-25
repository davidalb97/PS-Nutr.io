package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.clientError.DuplicateInsulinProfileException
import pt.isel.ps.g06.httpserver.common.exception.clientError.MissingInsulinProfileException
import pt.isel.ps.g06.httpserver.dataAccess.db.DbInsulinProfileDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.security.converter.ColumnCryptoConverter
import java.time.LocalTime
import java.time.OffsetDateTime

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val insulinProfileDaoClass = InsulinProfileDao::class.java

@Repository
class InsulinProfileDbRepository(
        val columnCryptoConverter: ColumnCryptoConverter,
        val dbInsulinProfileDtoMapper: DbInsulinProfileDtoMapper,
        jdbi: Jdbi
) : BaseDbRepo(jdbi) {

    fun getAllFromUser(submitterId: Int): Sequence<DbUserInsulinProfileDto> {
        val userInsulinProfiles = lazy {
            jdbi.inTransaction<Collection<DbUserInsulinProfileDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(insulinProfileDaoClass)
                        .getAllFromUser(submitterId)
                        .map(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)

            }
        }

        return Sequence { userInsulinProfiles.value.iterator() }
    }

    fun getFromUser(submitterId: Int, profileName: String): DbUserInsulinProfileDto {
        return jdbi.inTransaction<DbUserInsulinProfileDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .getFromUser(submitterId, profileName)
                    ?.let(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)
                    ?: throw MissingInsulinProfileException(profileName)
        }
    }

    /**
     * @throws DuplicateInsulinProfileException If an insulin profile with the name [profileName] already exists.
     */
    fun insertProfile(
            submitterId: Int,
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

            val encProfileName = columnCryptoConverter.convertToDatabaseColumn(profileName)
            val encStartTime = columnCryptoConverter.convertToDatabaseColumn(startTime.toString())
            val encEndTime = columnCryptoConverter.convertToDatabaseColumn(endTime.toString())
            val encGlucoseObjective = columnCryptoConverter.convertToDatabaseColumn(glucoseObjective.toString())
            val encInsulinSensitivityFactor = columnCryptoConverter.convertToDatabaseColumn(insulinSensitivityFactor.toString())
            val encCarbohydrateRatio = columnCryptoConverter.convertToDatabaseColumn(carbohydrateRatio.toString())
            val encModificationDate = columnCryptoConverter.convertToDatabaseColumn(OffsetDateTime.now().toString())

            return@inTransaction insulinProfileDao
                    .insertProfile(
                            submitterId = submitterId,
                            profileName = encProfileName,
                            startTime = encStartTime,
                            endTime = encEndTime,
                            glucoseObjective = encGlucoseObjective,
                            sensitivityFactor = encInsulinSensitivityFactor,
                            carbRatio = encCarbohydrateRatio,
                            modificationDate = encModificationDate
                    ).let(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)
        }
    }

    fun deleteProfile(submitterId: Int, profileName: String): DbUserInsulinProfileDto {
        return jdbi.inTransaction<DbUserInsulinProfileDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .deleteProfile(submitterId, profileName)
                    .let { encInsulinProfileDto ->
                        dbInsulinProfileDtoMapper.toDbUserInsulinProfileDto(
                                encInsulinProfileDto ?: throw MissingInsulinProfileException(profileName)
                        )
                    }
        }
    }
}