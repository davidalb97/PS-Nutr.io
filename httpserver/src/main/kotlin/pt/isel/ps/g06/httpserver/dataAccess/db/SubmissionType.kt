package pt.isel.ps.g06.httpserver.dataAccess.db

enum class SubmissionType(private val type: String) {
    RESTAURANT("Restaurant"),
    PORTION("Portion"),
    MEAL("Meal"),
    INGREDIENT("Ingredient"),
    CUISINE("Cuisine"),
    API_CUISINE("ApiCuisine"),
    RESTAURANT_MEAL("RestaurantMeal");

    override fun toString(): String = this.type
}