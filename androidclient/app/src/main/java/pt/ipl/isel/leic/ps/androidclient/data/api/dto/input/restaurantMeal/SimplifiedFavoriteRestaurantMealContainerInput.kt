package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurantMeal

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemInput

data class SimplifiedFavoriteRestaurantMealContainerInput(
    val meals: Collection<MealItemInput>
)