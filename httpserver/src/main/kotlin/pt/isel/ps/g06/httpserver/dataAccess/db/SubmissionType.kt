package pt.isel.ps.g06.httpserver.dataAccess.db

enum class SubmissionType(private val type: String) {
    RESTAURANT(SubmissionType.RESTAURANT_NAME),
    PORTION(SubmissionType.PORTION_NAME),
    MEAL(SubmissionType.MEAL_NAME),
    INGREDIENT(SubmissionType.INGREDIENT_NAME),
    CUISINE(SubmissionType.CUISINE_NAME),
    API_CUISINE(SubmissionType.API_CUISINE_NAME),
    RESTAURANT_MEAL(SubmissionType.RESTAURANT_MEAL_NAME);

    override fun toString(): String = this.type

    companion object {
        const val RESTAURANT_NAME = "Restaurant"
        const val PORTION_NAME = "Portion"
        const val MEAL_NAME = "Meal"
        const val INGREDIENT_NAME = "Ingredient"
        const val CUISINE_NAME = "Cuisine"
        const val API_CUISINE_NAME = "ApiCuisine"
        const val RESTAURANT_MEAL_NAME = "RestaurantMeal"
    }
}