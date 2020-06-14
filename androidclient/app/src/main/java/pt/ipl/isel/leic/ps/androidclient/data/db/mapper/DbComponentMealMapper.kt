package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

class DbComponentMealMapper {

    fun mapToModel(entity: DbComponentMealEntity) = MealIngredient(
        dbId = entity.primaryKey,
        dbMealId = entity.mealKey,
        submissionId = entity.submissionId,
        name = entity.name,
        isFavorite = entity.isFavorite,
        imageUri = entity.imageUri?.let { Uri.parse(it) },
        carbs = entity.carbs,
        amount = entity.amount,
        unit = entity.unit,
        isMeal = true
    )

    fun mapToEntity(model: MealIngredient) = DbComponentMealEntity(
        submissionId = model.submissionId,
        name = model.name,
        carbs = model.carbs,
        amount = model.amount,
        unit = model.unit,
        isFavorite = model.isFavorite,
        imageUri = model.imageUri?.toString()
    ).also { dto ->
        dto.primaryKey = model.dbId
        dto.mealKey = model.dbMealId
    }

    fun mapToListModel(relations: List<DbComponentMealEntity>) = relations.map(this::mapToModel)

    fun mapToListEntity(relations: List<MealIngredient>) = relations.map(this::mapToEntity)
}