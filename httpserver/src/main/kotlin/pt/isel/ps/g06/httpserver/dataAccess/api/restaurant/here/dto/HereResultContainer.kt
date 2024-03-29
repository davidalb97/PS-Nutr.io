package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto

import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto

data class HereResultContainer(val items: Collection<HereResultItem>)

class HereResultItem(
        title: String,
        id: String,
        position: Location,
        val foodTypes: Collection<FoodTypes>?
) : RestaurantDto(
        id = id,
        name = title,
        ownerId = null,
        latitude = position.lat,
        longitude = position.lng,
        //Here does not support image for restaurants
        image = null
)

data class Location(val lat: Float, val lng: Float)

data class FoodTypes(val id: String)