package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedIngredientInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedIngredientsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.*
import pt.ipl.isel.leic.ps.androidclient.data.util.appendPath
import pt.ipl.isel.leic.ps.androidclient.data.util.appendQueryNotNullParameter

class IngredientDataSource(
    private val requestParser: RequestParser
) {

    fun getIngredients(
        count: Int?,
        skip: Int?,
        success: (DetailedIngredientsInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(INGREDIENTS_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .build()
                .toString(),
            dtoClass = DetailedIngredientsInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getIngredientInfo(
        ingredientId: Int,
        success: (DetailedIngredientInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(INGREDIENTS_PATH)
                .appendPath(ingredientId)
                .build()
                .toString(),
            dtoClass = DetailedIngredientInput::class.java,
            onSuccess = success,
            onError = error
        )
    }
}