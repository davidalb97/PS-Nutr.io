package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem

class InputMealItemMapper(
    private val inputVotesMapper: InputVotesMapper
) {
    fun mapToModel(dto: SimplifiedMealInput) = MealItem(
            dbId = DbMealItemEntity.DEFAULT_DB_ID,
            dbRestaurantId = DbMealItemEntity.DEFAULT_DB_ID,
            submissionId = dto.mealIdentifier,
            //Nutritional info does not come in form of a MealItem,
            //this field is used for custom meal display
            carbs = null,
            amount = null,
            unit = null,
            imageUri = dto.imageUri,
            name = dto.name,
            votes = inputVotesMapper.mapToModel(dto.votes),
            isFavorite = dto.isFavorite,
            isSuggested = dto.isSuggested
    )

    fun mapToListModel(dtos: Iterable<SimplifiedMealInput>)
            = dtos.map(this::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedMealInput>)
            = dtos.map(this::mapToModel)

    fun mapToListModel(dto: SimplifiedMealsInput)
            = mapToListModel(dto.meals)

    fun mapToListModel(dto: SimplifiedRestaurantMealsInput)
            = mapToListModel(dto.suggestedMeals)
            .plus(mapToListModel(dto.userMeals))
}