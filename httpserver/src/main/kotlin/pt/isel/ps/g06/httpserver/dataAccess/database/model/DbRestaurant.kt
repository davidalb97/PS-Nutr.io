package pt.isel.ps.g06.httpserver.dataAccess.database.model

data class DbRestaurant(
        val restaurant_id: Int,
        val restaurant_name: String,
        val latitude: Float,
        val longitude: Float,
        val distance: Int = -1
)