package pt.isel.ps.g06.httpserver.common

const val RESTAURANT_ID_VALUE = "restaurantId"
const val MEAL_ID_VALUE = "mealId"
const val MEAL_NAME = "mealName"
const val SUBMITTER_ID_VALUE = "submitterId"
const val PROFILE_NAME_VALUE = "profileName"

//Restaurants
const val RESTAURANTS = "/restaurant"
const val RESTAURANT = "$RESTAURANTS/{$RESTAURANT_ID_VALUE}"
const val RESTAURANT_REPORT = "$RESTAURANT/report"
const val RESTAURANT_VOTE = "$RESTAURANT/vote"
const val RESTAURANT_MEALS = "$RESTAURANT/meal"
const val RESTAURANT_MEAL = "$RESTAURANT_MEALS/{$MEAL_ID_VALUE}"
const val RESTAURANT_MEAL_PORTION = "$RESTAURANT_MEAL/portion"
const val RESTAURANT_MEAL_REPORT = "$RESTAURANT_MEAL/report"
const val RESTAURANT_MEAL_VOTE = "$RESTAURANT_MEAL/vote"
const val AVAILABLE_RESTAURANT_TYPES = "$RESTAURANTS/availableTypes"

//Meals
const val MEALS = "/meal"
const val MEAL = "$MEALS/{$MEAL_ID_VALUE}"
const val MEAL_VOTE = "$MEAL/vote"
const val CUSTOM_MEALS = "/customMeal"
const val CUSTOM_MEAL = "/customMeal/{$MEAL_NAME}"

//Cuisines
const val CUISINES = "/cuisines"

//Ingredients
const val INGREDIENTS = "/ingredients"

//User
const val USER = "/user"
const val LOGIN = "$USER/login"
const val REGISTER = "$USER/register"
const val INSULIN_PROFILES = "$USER/profile"
const val INSULIN_PROFILE = "$USER/profile/{$PROFILE_NAME_VALUE}"

//Request utils
const val AUTH_HEADER = "Authorization"
const val BEARER = "Bearer "