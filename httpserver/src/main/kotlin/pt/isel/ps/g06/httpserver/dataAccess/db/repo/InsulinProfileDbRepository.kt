package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.problemJson.conflict.DuplicateInsulinProfileException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.MissingInsulinProfileException
import pt.isel.ps.g06.httpserver.dataAccess.db.DbInsulinProfileDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserEncInsulinProfileDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.security.converter.ColumnCryptoConverter
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.stream.Stream

private val insulinProfileDaoClass = InsulinProfileDao::class.java

@Repository
class InsulinProfileDbRepository(
        val columnCryptoConverter: ColumnCryptoConverter,
        val dbInsulinProfileDtoMapper: DbInsulinProfileDtoMapper,
        private val databaseContext: DatabaseContext
) {

    fun getAllFromUser(submitterId: Int, count: Int?, skip: Int?): Stream<DbUserInsulinProfileDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .getAllFromUser(submitterId, count, skip)
                    .map(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)
        }
    }

    fun getFromUser(submitterId: Int, profileName: String): DbUserInsulinProfileDto? {

        val encProfileName = columnCryptoConverter.convertToDatabaseColumn(profileName)

        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .getFromUser(submitterId, encProfileName)
                    ?.let(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)
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
        return databaseContext.inTransaction { handle ->
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

    fun deleteAllBySubmitter(submitterId: Int): Stream<DbUserEncInsulinProfileDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .deleteAllBySubmitterId(submitterId)
        }
    }

    fun deleteProfile(submitterId: Int, profileName: String): DbUserInsulinProfileDto {
        val encProfileName = columnCryptoConverter.convertToDatabaseColumn(profileName)

        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .deleteProfile(submitterId, encProfileName)
                    ?.let(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)
                    ?: throw MissingInsulinProfileException(profileName)
        }
    }
}