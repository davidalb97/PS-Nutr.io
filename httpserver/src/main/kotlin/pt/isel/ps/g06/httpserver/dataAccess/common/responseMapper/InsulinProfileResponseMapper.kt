package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.model.InsulinProfile

@Component
class InsulinProfileResponseMapper {

    fun mapToModel(dto: DbUserInsulinProfileDto): InsulinProfile =
            InsulinProfile(
                    submitterId = dto.submitterId,
                    profileName = dto.profileName,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    glucoseObjective = dto.glucoseObjective,
                    insulinSensitivityFactor = dto.insulinSensitivityFactor,
                    carbohydrateRatio = dto.carbohydrateRatio,
                    modificationDate = dto.modificationDate
            )
}