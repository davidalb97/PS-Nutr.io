package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.IngredientOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.MealOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RestaurantMealOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.VoteOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession


private const val RESTAURANT_ID_PARAM = ":restaurantId"
private const val MEAL_ID_PARAM = ":mealId"
private const val CUISINES_PARAM = ":cuisines"

private const val MEAL_URI = "$URI_BASE/$MEAL"
private const val MEAL_ID_URI = "$MEAL_URI/$MEAL_ID_PARAM"
private const val RESTAURANT_ID_URI = "$URI_BASE/$RESTAURANT/$RESTAURANT_ID_PARAM"
private const val RESTAURANT_ID_MEAL_URI = "$RESTAURANT_ID_URI/$MEAL"
private const val RESTAURANT_ID_MEAL_ID_URI = "$RESTAURANT_ID_MEAL_URI/$MEAL_ID_PARAM"
private const val RESTAURANT_ID_MEAL_ID_VOTE_URI = "$RESTAURANT_ID_MEAL_ID_URI/vote"

val INPUT_MEAL_DTO = DetailedMealInput::class.java
val OUTPUT_MEAL_DTO = MealOutput::class.java
val INPUT_MEALS_DTO = Array<SimplifiedMealInput>::class.java

class MealDataSource(
    private val requestParser: RequestParser
) {

    /**
     * Parameter: id is required
     */
    fun getAll(
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
            params.put(CUISINES_PARAM, cuisines.map { it.name }
                .joinToString(","))
        }
        uri = buildUri(uri, params)

        requestParser.requestAndRespond(
            method = Method.GET,
            uri = uri,
            dtoClass = SimplifiedMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    /**
     * Parameter: id is required
     */
    fun getAllByRestaurantId(
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

        requestParser.requestAndRespond(
            method = Method.GET,
            uri = uri,
            dtoClass = SimplifiedRestaurantMealsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }


    /**
     * ----------------------------- GETs -----------------------------
     */
    /**
     * Parameter: id is required
     */
    fun getMealById(
        mealId: Int,
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = MEAL_ID_URI
        val params = hashMapOf(
            Pair(MEAL_ID_PARAM, "$mealId")
        )
        uri = buildUri(uri, params)
        requestParser.requestAndRespond(
            method = Method.GET,
            uri = uri,
            dtoClass = INPUT_MEAL_DTO,
            onSuccess = success,
            onError = error
        )
    }

    fun getRestaurantMealById(
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
        requestParser.requestAndRespond(
            method = Method.GET,
            uri = uri,
            dtoClass = INPUT_MEAL_DTO,
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
        userSession: UserSession
    ) {

        // Composing the authorization header
        val reqHeader = buildAuthHeader(userSession.jwt)

        requestParser.request(
            method = Method.POST,
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
                        quantity = it.amount!!
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
        userSession: UserSession
    ) {
        val params = hashMapOf(
            Pair(RESTAURANT_ID_PARAM, restaurantId)
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(userSession.jwt)

        requestParser.requestAndRespond(
            method = Method.POST,
            uri = RESTAURANT_ID_URI,
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
        success: (Class<*>) -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        val uri =
            buildUri(
                MEAL_ID_URI,
                hashMapOf(
                    Pair(MEAL_ID_PARAM, "$mealId")
                )
            )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(userSession.jwt)

        requestParser.request(
            method = Method.DELETE,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = null,
            onError = error,
            responseConsumer = {}
        )
    }

    /**
     * Parameter: restaurantId & mealId are required
     */
    fun deleteRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        val uri = buildUri(
            RESTAURANT_ID_MEAL_ID_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(userSession.jwt)

        requestParser.request(
            method = Method.DELETE,
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
    fun updateVote(
        restaurantId: String,
        mealId: Int,
        vote: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        val uri = buildUri(
            RESTAURANT_ID_MEAL_ID_VOTE_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(userSession.jwt)

        requestParser.request(
            method = Method.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = VoteOutput(vote = vote),
            onError = error,
            responseConsumer = { success() }
        )
    }
}