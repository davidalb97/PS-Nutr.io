package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

enum class RestaurantApiType {
    Zomato,
    Here;

    companion object {
        fun getOrDefault(type: String?, default: RestaurantApiType = Here): RestaurantApiType {
            return values().find { it.toString().equals(type, true) } ?: default
        }

        fun getOrNull(type: String?): RestaurantApiType? {
            return values().find { it.toString().equals(type, true) }
        }
    }
}