package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemContainerInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurantMeal.SimplifiedRestaurantMealContainerInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.*
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.PayloadResponse
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.util.appendPath
import pt.ipl.isel.leic.ps.androidclient.data.util.appendQueryNotNullListParameter
import pt.ipl.isel.leic.ps.androidclient.data.util.appendQueryNotNullParameter

class MealDataSource(
    private val requestParser: RequestParser
) {

    /**
     * ----------------------------- GETs -----------------------------
     */

    fun getSuggestedMeals(
        jwt: String?,
        count: Int? = 30,
        skip: Int? = 0,
        cuisines: Collection<String>? = null,
        success: (MealItemContainerInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(SUGGESTED_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .appendQueryNotNullListParameter(CUISINES_PARAM, cuisines)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = MealItemContainerInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getRestaurantMeals(
        jwt: String?,
        restaurantId: String,
        count: Int? = 30,
        skip: Int? = 0,
        success: (SimplifiedRestaurantMealContainerInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = SimplifiedRestaurantMealContainerInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getMeal(
        mealId: Int,
        jwt: String?,
        success: (MealInfoInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = MealInfoInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        jwt: String?,
        success: (MealInfoInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = MealInfoInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getFavoriteMeals(
        jwt: String,
        count: Int? = 30,
        skip: Int? = 0,
        cuisines: Collection<String>? = null,
        success: (MealItemContainerInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(FAVORITE_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .appendQueryNotNullListParameter(CUISINES_PARAM, cuisines)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            dtoClass = MealItemContainerInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getCustomMeals(
        jwt: String,
        count: Int? = 30,
        skip: Int? = 0,
        cuisines: Collection<String>? = null,
        success: (MealItemContainerInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(CUSTOM_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .appendQueryNotNullListParameter(CUISINES_PARAM, cuisines)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            dtoClass = MealItemContainerInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */
    fun postCustomMeal(
        customMealOutput: CustomMealOutput,
        success: (PayloadResponse) -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(CUSTOM_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = customMealOutput,
            onError = error,
            responseConsumer = success
        )
    }

    fun postRestaurantMeal(
        restaurantId: String,
        restaurantMealOutput: RestaurantMealOutput,
        onSuccess: () -> Unit,
        onError: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            //TODO replace with POST once httpserver/improvements has been merged
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            onError = onError,
            responseConsumer = { onSuccess() },
            reqPayload = restaurantMealOutput
        )
    }

    fun postRestaurantMealPortion(
        restaurantId: String,
        mealId: Int,
        portionOutput: PortionOutput,
        onSuccess: (Int) -> Unit,
        onError: (VolleyError) -> Unit,
        jwt: String
    ) {

        requestParser.request(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .appendPath(PORTION_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = portionOutput,
            onError = onError,
            responseConsumer = { payloadResponse ->
                onSuccess(payloadResponse.status)
            }
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    fun deleteMeal(
        mealId: Int,
        success: (PayloadResponse) -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.DELETE,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = null,
            onError = error,
            responseConsumer = success
        )
    }

    fun deleteRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.DELETE,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = null,
            onError = error,
            responseConsumer = {}
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */

    fun putMeal(
        submissionId: Int,
        customMealOutput: CustomMealOutput,
        success: (PayloadResponse) -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(submissionId)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = customMealOutput,
            onError = error,
            responseConsumer = success
        )
    }

    fun putMealFavorite(
        mealId: Int,
        favoriteOutput: FavoriteOutput,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .appendPath(FAVORITE_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = favoriteOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantMealVote(
        restaurantId: String,
        mealId: Int,
        voteOutput: VoteOutput,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .appendPath(VOTE_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = voteOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantMealFavorite(
        restaurantId: String,
        mealId: Int,
        favoriteOutput: FavoriteOutput,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .appendPath(FAVORITE_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = favoriteOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantMealReport(
        restaurantId: String,
        mealId: Int,
        reportOutput: ReportOutput,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .appendPath(REPORT_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = reportOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantMealPortion(
        restaurantId: String,
        mealId: Int,
        portionOutput: PortionOutput,
        onSuccess: (Int) -> Unit,
        onError: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .appendPath(PORTION_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = portionOutput,
            onError = onError,
            responseConsumer = { onSuccess(it.status) }
        )
    }

    fun deleteRestaurantMealPortion(
        restaurantId: String,
        mealId: Int,
        onSuccess: (Int) -> Unit,
        onError: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.DELETE,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .appendPath(PORTION_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            onError = onError,
            responseConsumer = { onSuccess(it.status) }
        )
    }
}