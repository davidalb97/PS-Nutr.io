package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

data class MealComposition(
    val ingredients: Collection<DetailedRestaurantMealInput>,
    val meals: Collection<DetailedRestaurantMealInput>
)