package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.InsulinProfileInput
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone

class InputInsulinProfileMapper {

    fun mapToInputModel(dto: InsulinProfileInput) = InsulinProfile(
        dto.name,
        dto.startTime,
        dto.endTime,
        dto.glucoseObjective,
        dto.sensitivityFactor,
        dto.carbohydrateRatio,
        TimestampWithTimeZone.parse(dto.modificationTime)
    )

    fun mapToListInputModel(dtos: Iterable<InsulinProfileInput>) = dtos.map(::mapToInputModel)
}