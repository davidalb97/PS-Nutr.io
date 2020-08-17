package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedIngredientInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedIngredientsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.Source

class InputMealIngredientMapper {

    fun mapToModel(dto: DetailedIngredientInput) = MealIngredient(
        dbId = DbComponentIngredientEntity.DEFAULT_DB_ID,
        dbMealId = DbMealInfoEntity.DEFAULT_DB_ID,
        submissionId = dto.id,
        name = dto.name,
        imageUri = dto.imageUri,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        isMeal = false, //Not a meal (DetailedIngredientInput)
        source = Source.API
    )

    fun mapToModel(dto: DetailedMealInput, isMeal: Boolean) = MealIngredient(
        dbId = DbComponentIngredientEntity.DEFAULT_DB_ID,
        dbMealId = DbMealInfoEntity.DEFAULT_DB_ID,
        submissionId = dto.mealIdentifier,
        name = dto.name,
        imageUri = dto.imageUri,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        isMeal = isMeal,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<DetailedMealInput>, isMeal: Boolean): List<MealIngredient> =
        dtos.map { mapToModel(it, isMeal) }

    fun mapToListModel(dto: DetailedIngredientsInput): List<MealIngredient> =
        dto.ingredients.map(this::mapToModel)
}