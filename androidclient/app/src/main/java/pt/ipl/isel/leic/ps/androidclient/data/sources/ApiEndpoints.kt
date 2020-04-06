package pt.ipl.isel.leic.ps.androidclient.data.sources

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.sources.dtos.*
import pt.ipl.isel.leic.ps.androidclient.data.util.ServerRequest

interface ApiEndpoints {

    fun getRestaurants(
        success: (MutableList<RestaurantDto>) -> Unit,
        error: (VolleyError) -> Unit,
        userInput: Iterable<Pair<Any, String>>,
        count: Int
    ): ServerRequest<RestaurantsDto>

    fun getMeals(
        success: (MutableList<MealDto>) -> Unit,
        error: (VolleyError) -> Unit,
        userInput: Iterable<Pair<Any, String>>,
        count: Int
    ): ServerRequest<MealsDto>

    fun getCuisines(
        success: (MutableList<CuisineDto>) -> Unit,
        error: (VolleyError) -> Unit,
        userInput: Iterable<Pair<Any, String>>,
        count: Int
    ): ServerRequest<CuisinesDto>
}