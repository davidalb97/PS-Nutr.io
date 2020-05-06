package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.model

enum class RestaurantApiType {
    Zomato;

    companion object {
        fun getOrDefault(type: String?, default: RestaurantApiType = Zomato): RestaurantApiType {
            return try {
                return if (type == null) default
                else valueOf(type)
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}