package pt.isel.ps.g06.httpserver.dataAccess.output

data class MealComposition(
        val ingredients: Collection<DetailedRestaurantMealOutput>,
        val meals: Collection<DetailedRestaurantMealOutput>
)