package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

class DbRestaurantDto(
        submission_id: Int,
        restaurant_name: String,
        latitude: Float,
        longitude: Float
) : RestaurantDto(
        submission_id,
        restaurant_name,
        latitude,
        longitude,
        emptyList() //TODO This should be obtained by database right away because it's crucial to every restaurant - discuss.
)