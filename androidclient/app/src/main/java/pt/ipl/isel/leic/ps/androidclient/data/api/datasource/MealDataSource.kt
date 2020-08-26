package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.*
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.PayloadResponse
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
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
        success: (SimplifiedMealsInput) -> Unit,
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
            dtoClass = SimplifiedMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getRestaurantMeals(
        jwt: String?,
        restaurantId: String,
        count: Int? = 30,
        skip: Int? = 0,
        success: (SimplifiedRestaurantMealsInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = SimplifiedRestaurantMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getMeal(
        mealId: Int,
        jwt: String?,
        success: (DetailedMealInput) -> Unit,
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
            dtoClass = DetailedMealInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        jwt: String?,
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
                .appendPath(MEAL_PATH)
                .appendPath(mealId)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = DetailedMealInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getFavoriteMeals(
        jwt: String,
        count: Int? = 30,
        skip: Int? = 0,
        cuisines: Collection<String>? = null,
        success: (SimplifiedMealsInput) -> Unit,
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
            dtoClass = SimplifiedMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getCustomMeals(
        jwt: String,
        count: Int? = 30,
        skip: Int? = 0,
        cuisines: Collection<String>? = null,
        success: (SimplifiedMealsInput) -> Unit,
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
            dtoClass = SimplifiedMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */
    fun postCustomMeal(
        customMealOutput: CustomMealOutput,
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
            responseConsumer = { }
        )
    }

    fun postRestaurantMeal(
        restaurantId: String,
        restaurantMealOutput: RestaurantMealOutput,
        success: (CustomMealOutput) -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
                .appendPath(MEAL_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            dtoClass = CustomMealOutput::class.java,
            onSuccess = success,
            onError = error,
            reqPayload = restaurantMealOutput
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
                .appendPath(restaurantId)
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
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.POST,
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
            responseConsumer = { }
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
                .appendPath(restaurantId)
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
                .appendPath(restaurantId)
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
                .appendPath(restaurantId)
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
}