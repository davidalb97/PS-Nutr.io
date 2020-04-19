package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

enum class RestaurantApiType {
    Zomato;

    companion object {
        fun getType(type: String): RestaurantApiType? {
            return try {
                valueOf(type)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    } }