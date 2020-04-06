package pt.ipl.isel.leic.ps.androidclient.data.sources.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class RestaurantDto {
}

class RestaurantsDto (@JsonProperty("restaurants") val restaurantDtoList: MutableList<RestaurantDto>)