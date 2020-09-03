package pt.isel.ps.g06.httpserver.dataAccess.output.restaurant

import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant

class SimplifiedRestaurantContainerOutput(
        val restaurants: Collection<SimplifiedRestaurantOutput>
)

fun toSimplifiedRestaurantContainerOutput(
        restaurants: Collection<Restaurant>, userId: Int? = null
): SimplifiedRestaurantContainerOutput = SimplifiedRestaurantContainerOutput(
        restaurants = restaurants.map { toSimplifiedRestaurantOutput(it, userId) }
)