package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone

class InputMealInfoMapper(
    private val inputVotesMapper: InputVotesMapper,
    private val inputCuisineMapper: InputCuisineMapper,
    private val inputMealIngredientMapper: InputMealIngredientMapper,
    private val inputPortionMapper: InputPortionMapper
) {

    fun mapToModel(dto: DetailedMealInput, isSuggested: Boolean) = MealInfo(
        dbId = DbMealInfoEntity.DEFAULT_DB_ID,
        submissionId = dto.id,
        name = dto.name,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        votes = inputVotesMapper.mapToModel(dto.votes),
        isFavorite = dto.isFavorite,
        imageUri = dto.imageUri,
        creationDate = TimestampWithTimeZone.parse(dto.creationDate.toString()),
        ingredientComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.ingredients, false)
        } ?: emptyList(),
        mealComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.meals, true)
        } ?: emptyList(),
        cuisines = inputCuisineMapper.mapToListModel(dto.cuisines),
        portions = inputPortionMapper.mapToListModel(dto.portions),
        isSuggested = isSuggested
    )

    fun mapToListModel(dtos: Iterable<DetailedMealInput>, isSuggested: Boolean): List<MealInfo>
            = dtos.map { mapToModel(it, isSuggested)}
}