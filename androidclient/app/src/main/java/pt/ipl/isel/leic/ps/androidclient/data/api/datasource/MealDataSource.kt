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

private const val RESTAURANT_ID_PARAM = ":restaurantId"
private const val MEAL_ID_PARAM = ":mealId"
private const val COUNT_PARAM = ":count"
private const val SKIP_PARAM = ":skip"
private const val CUISINES_PARAM = ":cuisines"

private const val MEAL_URI = "$URI_BASE/$MEAL"
private const val MEAL_ID_URI = "$MEAL_URI/$MEAL_ID_PARAM"
private const val RESTAURANT_ID_URI = "$URI_BASE/$RESTAURANT/$RESTAURANT_ID_PARAM"
private const val RESTAURANT_ID_MEAL_URI = "$RESTAURANT_ID_URI/$MEAL"
private const val RESTAURANT_ID_MEAL_ID_URI = "$RESTAURANT_ID_MEAL_URI/$MEAL_ID_PARAM"

val INPUT_MEAL_DTO = DetailedMealInput::class.java
val OUTPUT_MEAL_DTO = MealOutput::class.java
val INPUT_MEALS_DTO = Array<SimplifiedMealInput>::class.java

class MealDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
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
        uri = uriBuilder.buildUri(uri, params)

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            SimplifiedMealsInput::class.java,
            success,
            error
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
        uri = uriBuilder.buildUri(uri, params)

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            SimplifiedRestaurantMealsInput::class.java,
            success,
            error
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
        uri = uriBuilder.buildUri(uri, params)
        requestParser.requestAndRespond(
            Method.GET,
            uri,
            INPUT_MEAL_DTO,
            success,
            error
        )
    }

    fun getRestaurantMealById(
        restaurantId: String,
        mealId: Int,
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        val uri = uriBuilder
            .buildUri(
                RESTAURANT_ID_MEAL_ID_URI,
                hashMapOf(
                    Pair(RESTAURANT_ID_PARAM, restaurantId),
                    Pair(MEAL_ID_PARAM, "$mealId")
                )
            )
        requestParser.requestAndRespond(
            Method.GET,
            uri,
            INPUT_MEAL_DTO,
            success,
            error
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
        error: (VolleyError) -> Unit
    ) {
        val uri = MEAL_URI

        requestParser.request(
            Method.POST,
            uri,
            MealOutput(
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
            error,
            { }
        )
    }

    fun postRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        success: (MealOutput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = RESTAURANT_ID_URI
        val params = hashMapOf(
            Pair(RESTAURANT_ID_PARAM, restaurantId)
        )
        uri = uriBuilder.buildUri(uri, params)
        requestParser.requestAndRespond(
            Method.POST,
            uri,
            OUTPUT_MEAL_DTO,
            success,
            error,
            {
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
        error: (VolleyError) -> Unit
    ) {

        val uri =
            uriBuilder.buildUri(
                MEAL_ID_URI,
                hashMapOf(
                    Pair(MEAL_ID_PARAM, "$mealId")
                )
            )
        requestParser.request(
            Method.DELETE,
            uri,
            null,
            error,
            {}
        )
    }

    /**
     * Parameter: restaurantId & mealId are required
     */
    fun deleteRestaurantMeal(
        restaurantId: String,
        mealId: Int,
        error: (VolleyError) -> Unit
    ) {

        val uri = uriBuilder.buildUri(
            RESTAURANT_ID_MEAL_ID_URI,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        requestParser.request(
            Method.DELETE,
            uri,
            null,
            error,
            {}
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */

    /**
     * Parameter: restaurantId & mealId are required
     */
    fun updateMealVote(
        restaurantId: String,
        mealId: Int,
        vote: Boolean,
        error: (VolleyError) -> Unit
    ) {
        var uri = RESTAURANT_ID_MEAL_ID_URI

        uri = uriBuilder.buildUri(
            uri,
            hashMapOf(
                Pair(RESTAURANT_ID_PARAM, restaurantId),
                Pair(MEAL_ID_PARAM, "$mealId")
            )
        )

        requestParser.request(
            Method.PUT,
            uri,
            VoteOutput(value = vote),
            error,
            { }
        )
    }
}