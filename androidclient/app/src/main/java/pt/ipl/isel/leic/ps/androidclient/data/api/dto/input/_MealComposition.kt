package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

data class _MealComposition(
    val ingredients: Collection<DetailedRestaurantMealInputDto>,
    val meals: Collection<DetailedRestaurantMealInputDto>
)