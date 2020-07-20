package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.model.InsulinProfile

@Component
class InsulinProfileMapper {

    fun mapToModel(dto: DbUserInsulinProfileDto): InsulinProfile =
            InsulinProfile(
                    submitterId = dto.submitterId,
                    profileName = dto.profileName,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    glucoseObjective = dto.glucoseObjective,
                    insulinSensitivityFactor = dto.insulinSensitivityFactor,
                    carbohydrateRatio = dto.carbohydrateRatio
            )

    fun mapToDto(model: InsulinProfile): DbUserInsulinProfileDto =
            DbUserInsulinProfileDto(
                    submitterId = model.submitterId,
                    profileName = model.profileName,
                    startTime = model.startTime,
                    endTime = model.endTime,
                    glucoseObjective = model.glucoseObjective,
                    insulinSensitivityFactor = model.insulinSensitivityFactor,
                    carbohydrateRatio = model.carbohydrateRatio
            )
}