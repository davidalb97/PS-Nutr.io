package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

data class SimplifiedRestaurantMealsInput(
    val restaurant: String,
    val suggestedMeals: Collection<SimplifiedMealInput>,
    val userMeals: Collection<SimplifiedMealInput>
)