package pt.isel.ps.g06.httpserver

import pt.isel.ps.g06.httpserver.model.Restaurant

class RestaurantFilter(private val restaurants: Collection<Restaurant>) {

    fun filter(cuisines: Collection<String> = emptyList(), meals: Collection<String> = emptyList()): Collection<Restaurant> {
        return restaurants.filter { restaurant ->
            restaurant.cuisines.none { cuisine -> cuisines.any { it.equals(cuisine, true) } }
        }
    }
}