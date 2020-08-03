package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source

class InputMealItemMapper(
    private val inputVotesMapper: InputVotesMapper
) {
    fun mapToModel(dto: SimplifiedMealInput, restaurantId: String?) = MealItem(
        dbId = DbMealItemEntity.DEFAULT_DB_ID,
        dbRestaurantId = DbMealItemEntity.DEFAULT_DB_ID,
        submissionId = dto.mealIdentifier,
        restaurantSubmissionId = restaurantId,
        //Nutritional info does not come in form of a MealItem,
        //this field is used for custom meal display
        carbs = null,
        amount = null,
        unit = null,
        imageUri = dto.imageUri,
        name = dto.name,
        votes = inputVotesMapper.mapToModel(dto.votes),
        isFavorite = dto.isFavorite,
        isSuggested = dto.isSuggested,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<SimplifiedMealInput>, restaurantId: String?) =
        dtos.map { mapToModel(it, restaurantId) }

    fun mapToListModel(dtos: Array<SimplifiedMealInput>, restaurantId: String?) =
        dtos.map { mapToModel(it, restaurantId) }

    fun mapToListModel(dto: SimplifiedMealsInput, restaurantId: String?) =
        mapToListModel(dto.meals, restaurantId)

    fun mapToListModel(dto: SimplifiedRestaurantMealsInput) =
        mapToListModel(dto.suggestedMeals, dto.restaurantIdentifier)
            .plus(mapToListModel(dto.userMeals, dto.restaurantIdentifier))
}