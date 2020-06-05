package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Ingredient

class DbIngredientMapper {

    fun mapToModel(entity: DbIngredientEntity) = Ingredient(
            dbId = entity.primaryKey,
            submissionId = entity.submissionId,
            name = entity.name,
            carbs = entity.carbs,
            amount = entity.amount,
            unit = entity.unit,
            imageUrl = entity.imageUrl
        )

    fun mapToRelation(model: Ingredient) = DbIngredientEntity(
            submissionId = model.submissionId,
            name = model.name,
            carbs = model.carbs,
            amount = model.amount,
            unit = model.unit,
            imageUrl = model.imageUrl
        ).also { dto ->
        //Set auto incremented serial
        dto.primaryKey = model.dbId
    }.also { dto ->
        dto.mealKey = model.dbMealId
    }

    fun mapToListModel(dtos: Iterable<DbIngredientEntity>): List<Ingredient> =
        dtos.map(this::mapToModel)

    fun mapToListDto(models: Iterable<Ingredient>): List<DbIngredientEntity> =
        models.map(this::mapToRelation)
}