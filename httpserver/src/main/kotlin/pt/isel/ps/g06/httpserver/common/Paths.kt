package pt.isel.ps.g06.httpserver.common

const val SUBMISSION_ID_VALUE = "submissionId"
const val RESTAURANT_ID_VALUE = "restaurantId"
const val MEAL_ID_VALUE = "mealId"
const val PROFILE_NAME_VALUE = "profileName"
const val REPORT_ID_VALUE = "reportId"

const val FAVORITE = "favorite"

//Restaurants
const val RESTAURANTS = "/restaurant"
const val RESTAURANT = "$RESTAURANTS/{$RESTAURANT_ID_VALUE}"
const val RESTAURANT_FAVORITE = "$RESTAURANT/$FAVORITE"
const val RESTAURANT_VOTE = "$RESTAURANT/vote"
const val RESTAURANT_REPORT = "$RESTAURANT/report"
const val RESTAURANT_MEALS = "$RESTAURANT/meal"
const val RESTAURANT_MEAL = "$RESTAURANT_MEALS/{$MEAL_ID_VALUE}"
const val RESTAURANT_MEAL_FAVORITE = "$RESTAURANT_MEAL/$FAVORITE"
const val RESTAURANT_MEAL_PORTION = "$RESTAURANT_MEAL/portion"
const val RESTAURANT_MEAL_REPORT = "$RESTAURANT_MEAL/report"
const val RESTAURANT_MEAL_VOTE = "$RESTAURANT_MEAL/vote"

//Meals
const val MEALS = "/meal"
const val MEAL = "$MEALS/{$MEAL_ID_VALUE}"
const val MEALS_SUGGESTED = "$MEALS/suggested"
const val MEALS_CUSTOM = "$MEALS/custom"
const val MEALS_FAVORITE = "$MEALS/$FAVORITE"
const val MEAL_FAVORITE = "$MEAL/$FAVORITE"

//Cuisines
const val CUISINES = "/cuisine"

//Ingredients
const val INGREDIENTS = "/ingredient"

//User
const val USER = "/user"
const val LOGIN = "$USER/login"
const val REGISTER = "$USER/register"
const val INSULIN_PROFILES = "$USER/profile"
const val INSULIN_PROFILE = "$USER/profile/{$PROFILE_NAME_VALUE}"

//Moderation
const val BAN = "$USER/ban"
const val REPORTS = "/report"
const val SUBMISSION_REPORT = "/report/{$SUBMISSION_ID_VALUE}"
const val REPORT = "/report/{$REPORT_ID_VALUE}"

//Submission
const val SUBMISSION = "submission/{$SUBMISSION_ID_VALUE}"