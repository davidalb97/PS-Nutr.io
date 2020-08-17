package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.*
import pt.ipl.isel.leic.ps.androidclient.data.api.request.*
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState


private const val RESTAURANT_ID_PARAM = ":restaurantId"
private const val MEAL_ID_PARAM = ":mealId"
private const val CUISINES_PARAM = ":cuisines"

private const val MEAL_URI = "$URI_BASE/$MEAL"
private const val MEAL_FAVORITE_URI = "$MEAL_URI/favorite"
private const val MEAL_ID_URI = "$MEAL_URI/$MEAL_ID_PARAM"
private const val MEAL_ID_FAVORITE_URI = "$MEAL_ID_URI/favorite"
private const val RESTAURANT_ID_URI = "$URI_BASE/$RESTAURANT/$RESTAURANT_ID_PARAM"
private const val RESTAURANT_ID_MEAL_URI = "$RESTAURANT_ID_URI/$MEAL"
private const val RESTAURANT_ID_MEAL_ID_URI = "$RESTAURANT_ID_MEAL_URI/$MEAL_ID_PARAM"
private const val RESTAURANT_ID_MEAL_ID_VOTE_URI = "$RESTAURANT_ID_MEAL_ID_URI/vote"
private const val RESTAURANT_ID_MEAL_ID_FAVORITE_URI = "$RESTAURANT_ID_MEAL_ID_URI/favorite"
private const val RESTAURANT_ID_MEAL_ID_REPORT_URI = "$RESTAURANT_ID_MEAL_ID_URI/report"

private val INPUT_MEAL_DTO = DetailedMealInput::class.java
private val OUTPUT_MEAL_DTO = MealOutput::class.java
private val INPUT_MEALS_DTO = Array<SimplifiedMealInput>::class.java

class MealDataSource(
    private val requestParser: RequestParser
) {

    /**
     * ----------------------------- GETs -----------------------------
     */

    /**
     * Parameter: id is required
     */
    fun getMeals(
        count: Int = 30,
        skip: Int = 0,
        cuisines: Collection<Cuisine>? = null,    //Optional filter
        success: (SimplifiedMealsInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = MEAL_URI
        val params = hashMapOf(
            Pair(SKIP_PARAM, "$skip"),
            Pair(COUNT_PARAM, "$count")
        )
        //If optional cuisines parameter was passed
        if (cuisines != null && cuisines.isNotEmpty()) {
            uri += "&cuisines=$CUISINES_PARAM"
            params[CUISINES_PARAM] = cuisines.joinToString(",") { it.name }
        }
        uri = buildUri(uri, params)

        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = uri,
            dtoClass = SimplifiedMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    /**
     * Parameter: id is required
     */
    fun getRestaurantMeals(
        restaurantId: String,
        count: Int = 30,
        skip: Int = 0,
        success: (SimplifiedRestaurantMealsInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = "$RESTAURANT_ID_MEAL_URI?skip=:skip&count=:count"
        val params = hashMapOf(
            Pair(RESTAURANT_ID_PARAM, restaurantId),
            Pair(SKIP_PARAM, "$skip"),
            Pair(COUNT_PARAM, "$count")
        )
        uri = buildUri(uri, params)

        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = uri,
            dtoClass = SimplifiedRestaurantMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }


    /**
     * Parameter: id is required
     */
    fun getMeal(
        mealId: Int,
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = MEAL_ID_URI
        val params = hashMapOf(
            Pair(MEAL_ID_PARAM, "$mealId")
        )
        uri = buildUri(uri, params)
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = uri,
            dtoClass = INPUT_MEAL_DTO,
            onSuccess = success,
            onError = error
        )
    }

    fun getRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        val uri = buildUri(
            RESTAURANT_ID_MEAL_ID_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = uri,
            dtoClass = INPUT_MEAL_DTO,
            onSuccess = success,
            onError = error
        )
    }

    fun getFavoriteMeals(
        jwt: String,
        count: Int = 30,
        skip: Int = 0,
        success: (SimplifiedRestaurantMealsInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = "$MEAL_FAVORITE_URI?skip=:skip&count=:count"
        val params = hashMapOf(
            Pair(SKIP_PARAM, "$skip"),
            Pair(COUNT_PARAM, "$count")
        )
        uri = buildUri(uri, params)
        val reqHeader = buildAuthHeader(jwt)

        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = uri,
            reqHeader = reqHeader,
            dtoClass = SimplifiedRestaurantMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */
    fun postMeal(
        name: String,
        quantity: Int,
        unit: String,
        ingredients: Iterable<MealIngredient>,
        cuisines: Iterable<Cuisine>,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.POST,
            uri = MEAL_URI,
            reqHeader = reqHeader,
            reqPayload = MealOutput(
                name = name,
                quantity = quantity,
                unit = unit,
                ingredients =
                ingredients.map {
                    IngredientOutput(
                        identifier = it.submissionId,
                        quantity = it.amount
                    )
                },
                cuisines = cuisines.map { it.name }
            ),
            onError = error,
            responseConsumer = { }
        )
    }

    fun postRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        success: (MealOutput) -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            RESTAURANT_ID_URI,
            hashMapOf(Pair(RESTAURANT_ID_PARAM, restaurantId))
        )
        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.requestAndParse(
            method = HTTPMethod.POST,
            uri = uri,
            reqHeader = reqHeader,
            dtoClass = OUTPUT_MEAL_DTO,
            onSuccess = success,
            onError = error,
            reqPayload = {
                //TODO replace with output mapper
                RestaurantMealOutput(
                    mealId = mealId
                )
            }
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */
    /**
     * Parameter: id is required
     */
    fun deleteMeal(
        mealId: Int,
        success: (PayloadResponse) -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri =
            buildUri(
                MEAL_ID_URI,
                hashMapOf(
                    Pair(MEAL_ID_PARAM, "$mealId")
                )
            )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.DELETE,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = null,
            onError = error,
            responseConsumer = success
        )
    }

    /**
     * Parameter: restaurantId & mealId are required
     */
    fun deleteRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            RESTAURANT_ID_MEAL_ID_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.DELETE,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = null,
            onError = error,
            responseConsumer = {}
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */

    /**
     * Parameter: restaurantId & mealId are required
     */
    fun putRestaurantMealVote(
        restaurantId: String,
        mealId: Int,
        vote: VoteState,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            RESTAURANT_ID_MEAL_ID_VOTE_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = VoteOutput(
                vote = when (vote) {
                    VoteState.NOT_VOTED -> null
                    VoteState.POSITIVE -> true
                    VoteState.NEGATIVE -> false
                }
            ),
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putMealFavorite(
        mealId: Int,
        isFavorite: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            MEAL_ID_FAVORITE_URI,
            hashMapOf(Pair(MEAL_ID_PARAM, "$mealId"))
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = FavoriteOutput(isFavorite = isFavorite),
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantMealFavorite(
        restaurantId: String,
        mealId: Int,
        isFavorite: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            RESTAURANT_ID_MEAL_ID_FAVORITE_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = FavoriteOutput(isFavorite = isFavorite),
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantMealReport(
        restaurantId: String,
        mealId: Int,
        reportStr: String,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            RESTAURANT_ID_MEAL_ID_REPORT_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = ReportOutput(description = reportStr),
            onError = error,
            responseConsumer = { success() }
        )
    }
}