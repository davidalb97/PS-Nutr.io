package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantItemDto
import pt.isel.ps.g06.httpserver.model.Votes

data class HereResultContainer(val items: Collection<HereResultItem>)

class HereResultItem(
        title: String,
        id: String,
        position: Location,
        val foodTypes: Collection<FoodTypes>?
) : RestaurantInfoDto(
        id = id,
        name = title,
        latitude = position.lat,
        longitude = position.lng,
        //There are no votes if it's not inserted on db yet
        votes = Votes(0, 0),
        //User has not voted yet if not inserted
        userVote = null,
        //User has not favored yet if not inserted
        isFavorite = false
)

data class Location(val lat: Float, val lng: Float)

data class FoodTypes(val id: String)