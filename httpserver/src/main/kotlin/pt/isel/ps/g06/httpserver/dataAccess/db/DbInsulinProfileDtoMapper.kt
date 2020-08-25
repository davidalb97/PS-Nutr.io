package pt.isel.ps.g06.httpserver.dataAccess.db

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserEncInsulinProfileDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.security.converter.ColumnCryptoConverter
import java.time.OffsetDateTime

@Component
class DbInsulinProfileDtoMapper(
        private val columnCryptoConverter: ColumnCryptoConverter
) {

    fun toDbUserInsulinProfileDto(dbUserEncInsulinProfileDto: DbUserEncInsulinProfileDto): DbUserInsulinProfileDto {
        return DbUserInsulinProfileDto(
                dbUserEncInsulinProfileDto.submitterId,
                columnCryptoConverter.convertToEntityAttribute(dbUserEncInsulinProfileDto.profileName),
                columnCryptoConverter.convertToEntityAttribute(dbUserEncInsulinProfileDto.startTime),
                columnCryptoConverter.convertToEntityAttribute(dbUserEncInsulinProfileDto.endTime),
                columnCryptoConverter.convertToEntityAttribute(dbUserEncInsulinProfileDto.glucoseObjective).toInt(),
                columnCryptoConverter.convertToEntityAttribute(dbUserEncInsulinProfileDto.insulinSensitivityFactor).toInt(),
                columnCryptoConverter.convertToEntityAttribute(dbUserEncInsulinProfileDto.carbohydrateRatio).toInt(),
                OffsetDateTime.parse(columnCryptoConverter.convertToEntityAttribute(dbUserEncInsulinProfileDto.modificationDate))
        )
    }
}