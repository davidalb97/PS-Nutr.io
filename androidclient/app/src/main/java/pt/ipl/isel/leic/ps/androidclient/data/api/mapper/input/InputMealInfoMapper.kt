package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

class InputMealInfoMapper(
    private val inputVotesMapper: InputVotesMapper,
    private val inputCuisineMapper: InputCuisineMapper,
    private val inputMealIngredientMapper: InputMealIngredientMapper,
    private val inputPortionMapper: InputPortionMapper,
    private val inputUserMapper: InputSubmissionOwnerMapper
) {

    fun mapToModel(dto: DetailedMealInput, restaurantId: String?) = MealInfo(
        dbId = DbMealInfoEntity.DEFAULT_DB_ID,
        dbRestaurantId = DbMealInfoEntity.DEFAULT_DB_ID,
        submissionId = dto.mealIdentifier,
        restaurantSubmissionId = restaurantId,
        name = dto.name,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount.toFloat(),
        unit = WeightUnits.fromValue(dto.nutritionalInfo.unit),
        votes = inputVotesMapper.mapToModel(dto.votes),
        isFavorite = dto.isFavorite,
        isVotable = dto.isVotable,
        imageUri = dto.imageUri,
        isSuggested = dto.isSuggested,
        creationDate = TimestampWithTimeZone.parse(dto.creationDate),
        ingredientComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.ingredients, false)
        } ?: emptyList(),
        mealComponents = dto.composedBy?.let {
            inputMealIngredientMapper.mapToListModel(it.meals, true)
        } ?: emptyList(),
        cuisines = dto.cuisines?.let { inputCuisineMapper.mapToListModel(dto.cuisines) }
            ?: emptyList(),
        portions = dto.portions?.let { inputPortionMapper.mapToListModel(it) } ?: emptyList(),
        source = Source.API,
        submissionOwner = dto.createdBy?.let(inputUserMapper::mapToModel)
    )

    fun mapToListModel(dtos: Iterable<DetailedMealInput>, restaurantId: String?): List<MealInfo> =
        dtos.map { mapToModel(it, restaurantId) }
}