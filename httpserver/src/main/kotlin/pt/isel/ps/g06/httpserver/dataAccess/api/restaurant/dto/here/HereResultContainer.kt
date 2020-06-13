package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

data class HereResultContainer(val items: Collection<HereResultItem>)

class HereResultItem(
        title: String,
        id: String,
        position: Location,
        val foodTypes: Collection<FoodTypes>?
) : RestaurantDto(
        id = id,
        name = title,
        latitude = position.lat,
        longitude = position.lng,
        //Here does not support image
        //TODO return restaurant image from Here result item
        image = null
)

data class Location(val lat: Float, val lng: Float)

data class FoodTypes(val id: String)