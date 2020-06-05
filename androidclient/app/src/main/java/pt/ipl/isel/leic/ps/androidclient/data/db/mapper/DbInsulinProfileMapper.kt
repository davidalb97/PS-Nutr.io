package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.InsulinProfileEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile

class DbInsulinProfileMapper {

    fun mapToModel(dto: InsulinProfileEntity) = InsulinProfile(
        dto.profileName,
        dto.startTime,
        dto.endTime,
        dto.glucoseObjective,
        dto.glucoseAmountPerInsulin,
        dto.carbsAmountPerInsulin
    )

    fun mapToRelation(model: InsulinProfile) = InsulinProfileEntity(
        model.profileName,
        model.startTime,
        model.endTime,
        model.glucoseObjective,
        model.glucoseAmountPerInsulin,
        model.carbsAmountPerInsulin
    )

    fun mapToListModel(dtos: Iterable<InsulinProfileEntity>) = dtos.map(::mapToModel)

    fun mapToListDto(models: Iterable<InsulinProfile>) = models.map(::mapToRelation)
}