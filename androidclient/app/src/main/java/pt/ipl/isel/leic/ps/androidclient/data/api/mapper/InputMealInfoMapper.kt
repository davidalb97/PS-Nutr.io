package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone

class InputMealInfoMapper(
    private val inputVotesMapper: InputVotesMapper,
    private val inputCuisineMapper: InputCuisineMapper,
    private val inputMealIngredientMapper: InputMealIngredientMapper,
    private val inputPortionMapper: InputPortionMapper
) {

    fun mapToModel(dto: DetailedMealInput) = MealInfo(
        dbId = DbMealInfoEntity.DEFAULT_DB_ID,
        dbRestaurantId = DbMealInfoEntity.DEFAULT_DB_ID,
        submissionId = dto.mealIdentifier,
        name = dto.name,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        votes = inputVotesMapper.mapToModel(dto.votes),
        isFavorite = dto.isFavorite,
        imageUri = dto.imageUri,
        isSuggested = dto.isSuggested,
        creationDate = TimestampWithTimeZone.parse(dto.creationDate),
        ingredientComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.ingredients, false)
        } ?: emptyList(),
        mealComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.meals, true)
        } ?: emptyList(),
        cuisines = dto.cuisines?.let { inputCuisineMapper.mapToListModel(dto.cuisines) } ?: emptyList(),
        portions = dto.portions?.let { inputPortionMapper.mapToListModel(it) } ?: emptyList(),
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<DetailedMealInput>): List<MealInfo>
            = dtos.map(this::mapToModel)
}