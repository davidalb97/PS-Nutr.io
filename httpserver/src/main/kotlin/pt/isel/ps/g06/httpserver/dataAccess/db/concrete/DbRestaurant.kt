package pt.isel.ps.g06.httpserver.dataAccess.db.concrete

data class DbRestaurant(
        val submission_id: Int,
        val restaurant_name: String,
        val latitude: Float,
        val longitude: Float
)