package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class InputMealItemMapper(
    private val inputVotesMapper: InputVotesMapper
) {
    fun mapToModel(dto: SimplifiedMealInput, restaurantId: String?) = MealItem(
        dbId = DbMealItemEntity.DEFAULT_DB_ID,
        dbRestaurantId = DbMealItemEntity.DEFAULT_DB_ID,
        submissionId = dto.mealIdentifier,
        restaurantSubmissionId = restaurantId,
        carbs = dto.nutritionalInfo.carbs,
        amount = dto.nutritionalInfo.amount.toFloat(),
        unit = WeightUnits.fromValue(dto.nutritionalInfo.unit),
        imageUri = dto.imageUri,
        name = dto.name,
        votes = inputVotesMapper.mapToModel(dto.votes),
        isFavorite = dto.isFavorite,
        isVotable = dto.isVotable,
        isSuggested = dto.isSuggested,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<SimplifiedMealInput>, restaurantId: String?) =
        dtos.map { mapToModel(it, restaurantId) }

    fun mapToListModel(dtos: Array<SimplifiedMealInput>, restaurantId: String?) =
        dtos.map { mapToModel(it, restaurantId) }

    fun mapToListModel(dto: SimplifiedMealsInput, restaurantId: String? = null) =
        mapToListModel(dto.meals, restaurantId)

    fun mapToListModel(dto: SimplifiedRestaurantMealsInput) =
        mapToListModel(dto.suggestedMeals, dto.restaurantIdentifier)
            .plus(mapToListModel(dto.userMeals, dto.restaurantIdentifier))
}