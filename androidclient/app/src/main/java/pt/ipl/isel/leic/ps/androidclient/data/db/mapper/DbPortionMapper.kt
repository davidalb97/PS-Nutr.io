package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbPortionEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Portion

class DbPortionMapper {

    fun mapToModel(entity: DbPortionEntity) = Portion(
        dbId = entity.primaryKey,
        dbMealId = entity.mealKey,
        amount = entity.amount
    )

    fun mapToEntity(model: Portion) = DbPortionEntity(
        amount = model.amount
    ).also { dto ->
        dto.primaryKey = model.dbId
        dto.mealKey = model.dbMealId
    }

    fun mapToListModel(entities: List<DbPortionEntity>) = entities.map(this::mapToModel)

    fun mapToListEntity(models: List<Portion>) = models.map(::mapToEntity)
}