package pt.isel.ps.g06.httpserver.common

const val INDEX_FILE_PATH = "index.html"
const val MAIN_FILE_PATH = "main.js"
const val SUBMISSION_ID_VALUE = "submissionId"
const val RESTAURANT_ID_VALUE = "restaurantId"
const val MEAL_ID_VALUE = "mealId"
const val PROFILE_NAME_VALUE = "profileName"
const val REPORT_ID_VALUE = "reportId"
const val FAVORITE = "favorite"

//All API calls are done under a base '/api' endpoint
const val BASE_API_PATH = "/api"

//Restaurants
const val RESTAURANT = "restaurant"
const val RESTAURANTS_PATH = "$BASE_API_PATH/$RESTAURANT"
const val RESTAURANT_ID_PATH = "$RESTAURANTS_PATH/{$RESTAURANT_ID_VALUE}"
const val RESTAURANT_FAVORITE_PATH = "$RESTAURANT_ID_PATH/$FAVORITE"
const val RESTAURANT_VOTE_PATH = "$RESTAURANT_ID_PATH/vote"
const val RESTAURANT_REPORT_PATH = "$RESTAURANT_ID_PATH/report"
const val RESTAURANT_MEALS_PATH = "$RESTAURANT_ID_PATH/meal"
const val RESTAURANT_MEAL_ID_PATH = "$RESTAURANT_MEALS_PATH/{$MEAL_ID_VALUE}"
const val RESTAURANT_MEAL_FAVORITE_PATH = "$RESTAURANT_MEAL_ID_PATH/$FAVORITE"
const val RESTAURANT_MEAL_PORTION_PATH = "$RESTAURANT_MEAL_ID_PATH/portion"
const val RESTAURANT_MEAL_REPORT_PATH = "$RESTAURANT_MEAL_ID_PATH/report"
const val RESTAURANT_MEAL_VOTE_PATH = "$RESTAURANT_MEAL_ID_PATH/vote"

//Meals
const val MEAL = "meal"
const val MEALS_PATH = "$BASE_API_PATH/$MEAL"
const val MEAL_ID_PATH = "$MEALS_PATH/{$MEAL_ID_VALUE}"
const val MEAL_ID_FAVORITE_PATH = "$MEAL_ID_PATH/$FAVORITE"

//Cuisines
const val CUISINES_PATH = "$BASE_API_PATH/cuisine"

//Ingredients
const val INGREDIENTS_PATH = "$BASE_API_PATH/ingredient"

//User
const val USER = "user"
const val PROFILE = "profile"
const val CUSTOM = "custom"
const val USER_PATH = "$BASE_API_PATH/$USER"
const val LOGIN_PATH = "$USER_PATH/login"
const val REGISTER_PATH = "$USER_PATH/register"
const val INSULIN_PROFILES = "$USER_PATH/$PROFILE"
const val INSULIN_PROFILE = "$USER_PATH/$PROFILE/{$PROFILE_NAME_VALUE}"
const val USER_CUSTOM_MEALS_PATH = "$USER_PATH/$CUSTOM/$MEAL"
const val USER_CUSTOM_MEAL_ID_PATH = "$USER_CUSTOM_MEALS_PATH/{$MEAL_ID_VALUE}"
const val USER_FAVORITE_PATH = "$USER_PATH/$FAVORITE"
const val USER_FAVORITE_MEALS_PATH = "$USER_FAVORITE_PATH/$MEAL"
const val USER_FAVORITE_RESTAURANTS_PATH = "$USER_FAVORITE_PATH/$RESTAURANT"
const val USER_FAVORITE_RESTAURANT_MEALS_PATH = "$USER_FAVORITE_PATH/$RESTAURANT/$MEAL"

//Moderation
const val BAN_PATH = "$USER_PATH/ban"
const val REPORT = "report"
const val REPORTS_PATH = "$BASE_API_PATH/$REPORT"
const val REPORT_SUBMISSION_ID = "$REPORTS_PATH/{$SUBMISSION_ID_VALUE}"
const val REPORT_ID = "$REPORTS_PATH/{$REPORT_ID_VALUE}"

//Submission
const val SUBMISSION = "submission"
const val SUBMISSION_PATH = "$BASE_API_PATH/$SUBMISSION/{$SUBMISSION_ID_VALUE}"