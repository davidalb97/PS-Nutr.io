package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.DbInsulinProfileDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.exception.problemJson.conflict.DuplicateInsulinProfileException
import pt.isel.ps.g06.httpserver.exception.problemJson.notFound.MissingInsulinProfileException
import pt.isel.ps.g06.httpserver.security.converter.ColumnCryptoConverter
import pt.isel.ps.g06.httpserver.util.ClosableSequence
import pt.isel.ps.g06.httpserver.util.asClosableSequence
import java.time.LocalTime
import java.time.OffsetDateTime

private val insulinProfileDaoClass = InsulinProfileDao::class.java

@Repository
class InsulinProfileDbRepository(
        val columnCryptoConverter: ColumnCryptoConverter,
        val dbInsulinProfileDtoMapper: DbInsulinProfileDtoMapper,
        private val databaseContext: DatabaseContext
) {

    fun getAllFromUser(submitterId: Int, count: Int?, skip: Int?): ClosableSequence<DbUserInsulinProfileDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .getAllFromUser(submitterId, count, skip)
                    .map(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)
                    .asClosableSequence()
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
            glucoseObjective: Float,
            insulinSensitivityFactor: Float,
            carbohydrateRatio: Float
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

    fun deleteAllBySubmitter(submitterId: Int) {
        return databaseContext.inTransaction { handle ->
            handle.attach(insulinProfileDaoClass)
                    .deleteAllBySubmitterId(submitterId)
        }
    }

    fun deleteProfile(submitterId: Int, profileName: String): DbUserInsulinProfileDto {
        val encProfileName = columnCryptoConverter.convertToDatabaseColumn(profileName)

        return databaseContext.inTransaction { handle ->
            val dao = handle.attach(insulinProfileDaoClass)
            if(dao.getFromUser(submitterId, encProfileName) == null) {
                throw MissingInsulinProfileException(profileName)
            }
            //TODO Get insulin profile before deleting it
            return@inTransaction dao.deleteProfile(submitterId, encProfileName)
                    ?.let(dbInsulinProfileDtoMapper::toDbUserInsulinProfileDto)
                    ?: throw MissingInsulinProfileException(profileName)
        }
    }
}