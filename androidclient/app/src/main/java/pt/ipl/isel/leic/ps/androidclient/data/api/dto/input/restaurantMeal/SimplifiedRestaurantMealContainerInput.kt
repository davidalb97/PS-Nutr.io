package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurantMeal

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemInput

data class SimplifiedRestaurantMealContainerInput(
    val restaurantIdentifier: String,
    val suggestedMeals: Collection<MealItemInput>,
    val userMeals: Collection<MealItemInput>
)