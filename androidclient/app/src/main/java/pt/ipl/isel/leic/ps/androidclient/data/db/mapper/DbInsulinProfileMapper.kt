package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfileEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile

class DbInsulinProfileMapper {

    fun mapToModel(dto: DbInsulinProfileEntity) = InsulinProfile(
        dto.profileName,
        dto.startTime,
        dto.endTime,
        dto.glucoseObjective,
        dto.glucoseAmountPerInsulin,
        dto.carbsAmountPerInsulin,
        dto.modificationDate
    )

    fun mapToRelation(model: InsulinProfile) = DbInsulinProfileEntity(
        model.profileName,
        model.startTime,
        model.endTime,
        model.glucoseObjective,
        model.glucoseAmountPerInsulin,
        model.carbsAmountPerInsulin,
        model.modificationDate
    )

    fun mapToListModel(dtos: Iterable<DbInsulinProfileEntity>) = dtos.map(::mapToModel)

    fun mapToListRelation(models: Iterable<InsulinProfile>) = models.map(::mapToRelation)
}