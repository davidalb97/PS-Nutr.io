package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemContainerInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurantMeal.SimplifiedRestaurantMealContainerInput
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class InputMealItemMapper(
    private val inputVotesMapper: InputVotesMapper,
    private val inputFavoriteMapper: InputFavoriteMapper
) {

    fun mapToModel(dto: MealItemInput, restaurantId: String?) = MealItem(
        dbId = null,
        dbRestaurantId = null,
        submissionId = dto.identifier,
        restaurantSubmissionId = restaurantId,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount.toFloat(),
        unit = WeightUnits.fromValue(dto.nutritionalInfo.unit),
        imageUri = dto.image,
        name = dto.name,
        votes = inputVotesMapper.mapToModel(dto.votes),
        favorites = inputFavoriteMapper.mapToModel(dto.favorites),
        isVerified = dto.isVerified ?: false,
        isSuggested = dto.isSuggested ?: false,
        isReportable = dto.isReportable ?: false,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<MealItemInput>, restaurantId: String?) =
        dtos.map { mapToModel(it, restaurantId) }

    fun mapToListModel(dtos: Collection<MealItemInput>, restaurantId: String?) =
        dtos.map { mapToModel(it, restaurantId) }

    fun mapToListModel(dtos: Array<MealItemInput>, restaurantId: String?) =
        dtos.map { mapToModel(it, restaurantId) }

    fun mapToListModel(dto: MealItemContainerInput, restaurantId: String? = null) =
        mapToListModel(dto.meals, restaurantId)

    fun mapToListModel(dto: SimplifiedRestaurantMealContainerInput) =
        mapToListModel(dto.suggestedMeals, dto.restaurantIdentifier)
            .plus(mapToListModel(dto.userMeals, dto.restaurantIdentifier))
}