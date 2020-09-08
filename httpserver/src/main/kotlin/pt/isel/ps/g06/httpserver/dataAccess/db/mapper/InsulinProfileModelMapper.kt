package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.model.InsulinProfile

@Component
class InsulinProfileModelMapper : ModelMapper<DbUserInsulinProfileDto, InsulinProfile> {
    override fun mapTo(dto: DbUserInsulinProfileDto): InsulinProfile {
        return InsulinProfile(
                identifier = dto.submitterId,
                name = dto.profileName,
                startTime = dto.startTime,
                endTime = dto.endTime,
                glucoseObjective = dto.glucoseObjective,
                insulinSensitivityFactor = dto.insulinSensitivityFactor,
                carbohydrateRatio = dto.carbohydrateRatio,
                modificationDate = dto.modificationDate
        )
    }
}