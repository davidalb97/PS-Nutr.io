package pt.isel.ps.g06.httpserver.dataAccess.db.concrete

import pt.isel.ps.g06.httpserver.dataAccess.RestaurantDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantResponse

data class DbRestaurant(
        val submission_id: Int,
        val restaurant_name: String,
        val latitude: Float,
        val longitude: Float
) : RestaurantDtoMapper() {

    override fun mapDto(): RestaurantResponse {
        return RestaurantResponse(
                submission_id,
                restaurant_name,
                latitude,
                longitude,
                emptyArray()
        )
    }
}