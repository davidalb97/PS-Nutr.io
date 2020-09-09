package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

class InputMealInfoMapper(
    private val inputCuisineMapper: InputCuisineMapper,
    private val inputMealIngredientMapper: InputMealIngredientMapper,
    private val inputUserMapper: InputSubmissionOwnerMapper,
    private val inputFavoriteMapper: InputFavoriteMapper,
    private val inputPortionMapper: InputPortionMapper
) {

    fun mapToModel(dto: MealInfoInput, restaurantId: String?) = MealInfo(
        dbId = null,
        dbRestaurantId = null,
        submissionId = dto.identifier,
        restaurantSubmissionId = restaurantId,
        name = dto.name,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = WeightUnits.fromValue(dto.nutritionalInfo.unit),
        votes = null,
        favorites = inputFavoriteMapper.mapToModel(dto.favorites),
        imageUri = dto.image,
        isSuggested = dto.isSuggested ?: false,
        creationDate = TimestampWithTimeZone.parse(dto.creationDate),
        ingredientComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.ingredients, false)
        } ?: emptyList(),
        mealComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.meals, true)
        } ?: emptyList(),
        cuisines = dto.cuisines?.let { inputCuisineMapper.mapToListModel(it) } ?: emptyList(),
        portions = dto.portions?.let { inputPortionMapper.mapToModel(it) },
        isVerified = dto.isVerified ?: false,
        isReportable = dto.isReportable ?: false,
        source = Source.API,
        submissionOwner = dto.createdBy?.let(inputUserMapper::mapToModel)
    )

    fun mapToListModel(dtos: Iterable<MealInfoInput>, restaurantId: String?): List<MealInfo> =
        dtos.map { mapToModel(it, restaurantId) }
}