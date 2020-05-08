package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.model

enum class RestaurantApiType {
    Zomato,
    Here;

    companion object {
        fun getOrDefault(type: String?, default: RestaurantApiType = Here): RestaurantApiType {
            return if (type == null) default
            else values().find { it.toString().equals(type, true) } ?: default
        }
    }
}