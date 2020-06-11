package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.model.RestaurantInfo
import pt.isel.ps.g06.httpserver.model.RestaurantItem

data class SimplifiedRestaurantOutputModel(
        val identifier: String,
        val name: String,
        val latitude: Float,
        val longitude: Float
)


fun toSimplifiedRestaurant(restaurant: RestaurantItem): SimplifiedRestaurantOutputModel {
    return SimplifiedRestaurantOutputModel(
            identifier = restaurant.identifier.toString(),
            name = restaurant.name,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude
    )
}