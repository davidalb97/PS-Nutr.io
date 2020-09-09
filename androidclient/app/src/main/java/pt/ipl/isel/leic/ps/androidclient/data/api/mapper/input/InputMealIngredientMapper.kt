package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.ingredient.IngredientContainerInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class InputMealIngredientMapper(
    private val inputVotesMapper: InputVotesMapper,
    private val inputFavoritesMapper: InputFavoriteMapper
) {

    fun mapToModel(dto: MealItemInput, isMeal: Boolean) = MealIngredient(
        dbId = null,
        dbMealId = null,
        submissionId = dto.identifier,
        name = dto.name,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = WeightUnits.fromValue(dto.nutritionalInfo.unit),
        imageUri = dto.image,
        favorites = inputFavoritesMapper.mapToModel(dto.favorites),
        isMeal = isMeal,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<MealItemInput>, isMeal: Boolean): List<MealIngredient> =
        dtos.map { mapToModel(it, isMeal) }

    fun mapToListModel(dto: IngredientContainerInput): List<MealIngredient> =
        dto.ingredients.map { mapToModel(it, false) }
}