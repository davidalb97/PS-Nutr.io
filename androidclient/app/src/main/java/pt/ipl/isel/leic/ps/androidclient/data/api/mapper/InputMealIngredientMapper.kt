package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

class InputMealIngredientMapper {

    fun mapToModel(dto: DetailedMealInput, isMeal: Boolean) = MealIngredient(
        dbId = DbComponentIngredientEntity.DEFAULT_DB_ID,
        dbMealId = DbMealInfoEntity.DEFAULT_DB_ID,
        submissionId = dto.id,
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