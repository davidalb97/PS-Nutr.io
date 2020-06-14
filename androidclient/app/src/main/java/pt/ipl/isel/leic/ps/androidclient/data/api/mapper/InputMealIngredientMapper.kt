package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

class InputMealIngredientMapper {

    fun mapToModel(dto: DetailedMealInput, isMeal: Boolean) = MealIngredient(
        submissionId = dto.id,
        dbId = DbMealInfoEntity.DEFAULT_DB_ID,
        name = dto.name,
        isFavorite = dto.isFavorite,
        imageUri = dto.imageUri,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        isMeal = isMeal
    )

    fun mapToListModel(dtos: Iterable<DetailedMealInput>, isMeal: Boolean): List<MealIngredient>
            = dtos.map { mapToModel(it, isMeal) }
}