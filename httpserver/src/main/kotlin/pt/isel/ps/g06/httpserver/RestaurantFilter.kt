package pt.isel.ps.g06.httpserver

import pt.isel.ps.g06.httpserver.model.RestaurantInfo

class RestaurantFilter(private val restaurants: Collection<RestaurantInfo>) {

    fun filter(cuisines: Collection<String> = emptyList(), meals: Collection<String> = emptyList()): Collection<RestaurantInfo> {
        return restaurants
//        return restaurants.filter { restaurant ->
//            restaurant.cuisines.none { cuisine -> cuisines.any { it.equals(cuisine, true) } }
//        }
    }
}