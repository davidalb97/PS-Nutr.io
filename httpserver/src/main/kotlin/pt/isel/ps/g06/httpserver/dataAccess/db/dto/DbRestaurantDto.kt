package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

open class DbRestaurantDto(
        val submission_id: Int,
        restaurant_name: String,
        ownerId: Int,
        latitude: Float,
        longitude: Float
) : RestaurantDto(
        id = "$submission_id",
        name = restaurant_name,
        ownerId = ownerId,
        longitude = longitude,
        latitude = latitude,
        //Db does not support image
        //TODO return db image from restaurant table
        image = null
)