package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

data class HereResultContainer(val items: Collection<HereResultItem>)

class HereResultItem(
        title: String,
        id: String,
        position: Location
) : RestaurantDto(id, title, position.lat, position.lng)

data class Location(val lat: Float, val lng: Float)