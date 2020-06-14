package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedIngredientInput

private const val COUNT_PARAM = ":count"
private const val SKIP_PARAM = ":skip"
private const val INGREDIENT_URI ="$URI_BASE/$INGREDIENTS"
private const val INGREDIENT_LIST_URI ="$URI_BASE/$INGREDIENTS" +
        "?skip=$SKIP_PARAM" +
        "&count=$COUNT_PARAM"
private const val INGREDIENT_ID_PARAM = ":id"
private const val INGREDIENT_ID_URI = "$INGREDIENT_URI/$INGREDIENT_ID_PARAM"

class IngredientDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getIngredients(
        count: Int,
        skip: Int,
        success: (Array<String>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = INGREDIENT_LIST_URI
        val params = hashMapOf(
            Pair(SKIP_PARAM, "$skip"),
            Pair(COUNT_PARAM, "$count")
        )
        uri = uriBuilder.buildUri(uri, params)
        requestParser.requestAndRespond(
            Method.GET,
            uri,
            Array<String>::class.java,
            success,
            error
        )
    }

    /**
     * Required id parameter
     */
    fun getIngredientInfo(
        ingredientId: Int,
        success: (DetailedIngredientInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = INGREDIENT_ID_URI
        val params = hashMapOf(Pair(
            INGREDIENT_ID_PARAM,
            "$ingredientId"
        ))
        uri = uriBuilder.buildUri(uri, params)

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            DetailedIngredientInput::class.java,
            success,
            error
        )
    }
}