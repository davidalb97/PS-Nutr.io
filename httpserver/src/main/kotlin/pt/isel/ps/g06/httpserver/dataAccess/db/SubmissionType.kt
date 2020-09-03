package pt.isel.ps.g06.httpserver.dataAccess.db

const val SUBMISSION_TYPE_RESTAURANT = "Restaurant"
const val SUBMISSION_TYPE_PORTION = "Portion"
const val SUBMISSION_TYPE_MEAL = "Meal"
const val SUBMISSION_TYPE_CUISINE = "Cuisine"
const val SUBMISSION_TYPE_API_CUISINE = "ApiCuisine"
const val SUBMISSION_TYPE_RESTAURANT_MEAL = "RestaurantMeal"
val REPORTABLE_TYPES = listOf(SubmissionType.RESTAURANT_MEAL.toString(), SubmissionType.RESTAURANT.toString())

enum class SubmissionType(private val type: String) {
    RESTAURANT(SUBMISSION_TYPE_RESTAURANT),
    PORTION(SUBMISSION_TYPE_PORTION),
    MEAL(SUBMISSION_TYPE_MEAL),
    CUISINE(SUBMISSION_TYPE_CUISINE),
    API_CUISINE(SUBMISSION_TYPE_API_CUISINE),
    RESTAURANT_MEAL(SUBMISSION_TYPE_RESTAURANT_MEAL);

    override fun toString(): String = this.type
}