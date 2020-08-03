package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

class DbComponentIngredientMapper {

    fun mapToModel(entity: DbComponentIngredientEntity) = MealIngredient(
        dbId = entity.primaryKey,
        dbMealId = entity.mealKey,
        submissionId = entity.submissionId,
        name = entity.name,
        isFavorite = entity.isFavorite,
        imageUri = entity.imageUri?.let { Uri.parse(it) },
        carbs = entity.carbs,
        amount = entity.amount,
        unit = entity.unit,
        isMeal = false
    )

    fun mapToEntity(model: MealIngredient) = DbComponentIngredientEntity(
        submissionId = model.submissionId,
        name = model.name,
        carbs = model.carbs!!,
        amount = model.amount!!,
        unit = model.unit!!,
        isFavorite = model.isFavorite,
        imageUri = model.imageUri?.toString()
    ).also { dto ->
        dto.primaryKey = model.dbId
        dto.mealKey = model.dbMealId
    }

    fun mapToListModel(relations: List<DbComponentIngredientEntity>) =
        relations.map(this::mapToModel)

    fun mapToListEntity(relations: List<MealIngredient>) = relations.map(this::mapToEntity)
}