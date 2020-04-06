package pt.ipl.isel.leic.ps.androidclient.data.sources

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.sources.dtos.*
import pt.ipl.isel.leic.ps.androidclient.data.util.ServerRequest

const val ADDRESS = "TODO"
const val PORT = "TODO"
const val GET = Request.Method.GET
const val POST = Request.Method.POST
const val DELETE = Request.Method.DELETE
const val PUT = Request.Method.PUT
const val AND = "&"
const val SKIP = "skip="
const val COUNT = "count="
const val SLASH = "/"
const val QUERY = "?"
const val RESTAURANT = "restaurant"
const val MEAL = "meal"
const val CUISINES = "cuisines"
const val INGREDIENTS = "ingredients"
const val USER_QUERY = "user"


class ApiRequester : ApiEndpoints {

    /**
     * GETs
     */
    override fun getRestaurants(
        success: (MutableList<RestaurantDto>) -> Unit,
        error: (VolleyError) -> Unit,
        userInput: Iterable<Pair<Any, String>>,
        count: Int
    ): ServerRequest<RestaurantsDto> {
        return ServerRequest(
            GET,
            "",
            RestaurantsDto::class.java,
            Response.Listener {
                success(it.restaurantDtoList)
            },
            Response.ErrorListener {
                error(it)
            }
        )
    }

    override fun getMeals(
        success: (MutableList<MealDto>) -> Unit,
        error: (VolleyError) -> Unit,
        userInput: Iterable<Pair<Any, String>>,
        count: Int
    ): ServerRequest<MealsDto> {
        return ServerRequest(
            GET,
            "",
            MealsDto::class.java,
            Response.Listener {
                success(it.mealDtoList)
            },
            Response.ErrorListener {
                error(it)
            }
        )
    }

    override fun getCuisines(
        success: (MutableList<CuisineDto>) -> Unit,
        error: (VolleyError) -> Unit,
        userInput: Iterable<Pair<Any, String>>,
        count: Int
    ): ServerRequest<CuisinesDto> {
        return ServerRequest(
            GET,
            "",
            CuisinesDto::class.java,
            Response.Listener {
                success(it.cuisineDtoList)
            },
            Response.ErrorListener {
                error(it)
            }
        )
    }

    /**
     * POSTs
     */

    /**
     * DELETEs
     */

    /**
     * PUTs
     */
}
