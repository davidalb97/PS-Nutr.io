package pt.isel.ps.g06.httpserver.dataAccess.database.model

import pt.isel.ps.g06.httpserver.model.BaseRestaurant

data class DbRestaurant(
        val restaurant_id: Int,
        val restaurant_name: String,
        val latitude: Float,
        val longitude: Float,
        val distance: Int = -1
) : BaseRestaurant() {
    override fun getRestaurantId(): Int = restaurant_id
    override fun getRestaurantName(): String = restaurant_name
    override fun getRestaurantLatitude(): Float = latitude
    override fun getRestaurantLongitude(): Float = longitude
}