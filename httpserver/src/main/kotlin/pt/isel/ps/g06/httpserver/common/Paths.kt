package pt.isel.ps.g06.httpserver.common

const val RESTAURANT_ID_VALUE = "restaurantId"
const val MEAL_ID_VALUE = "mealId"
const val PROFILE_NAME_VALUE = "profileName"
const val USER_EMAIL_VALUE = "profileName"

//Restaurants
const val RESTAURANTS = "/restaurant"
const val RESTAURANT = "$RESTAURANTS/{$RESTAURANT_ID_VALUE}"
const val RESTAURANT_VOTE = "$RESTAURANT/vote"
const val RESTAURANT_MEALS = "$RESTAURANT/meal"
const val RESTAURANT_MEAL = "$RESTAURANT_MEALS/{$MEAL_ID_VALUE}"
const val RESTAURANT_MEAL_PORTION = "$RESTAURANT_MEAL/portion"
const val RESTAURANT_MEAL_VOTE = "$RESTAURANT_MEAL/vote"

//Meals
const val MEALS = "/meal"
const val MEAL = "$MEALS/{$MEAL_ID_VALUE}"

//Cuisines
const val CUISINES = "/cuisines"

//Ingredients
const val INGREDIENTS = "/ingredients"

//User
const val USER = "/user"
const val LOGIN = "$USER/login"
const val REGISTER = "$USER/register"
const val USER_INFO = "$USER/info"
const val INSULIN_PROFILES = "$USER/profile"
const val INSULIN_PROFILE = "$USER/profile/{$PROFILE_NAME_VALUE}"

//Moderation
const val BAN = "$USER/{$USER_EMAIL_VALUE}"
const val REPORTS = "/report"