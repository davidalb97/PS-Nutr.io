package pt.isel.ps.g06.httpserver.dataAccess

enum class RestaurantApiType {
    ZOMATO
}

private val enumMapper  = mapOf(
        Pair("zomato", RestaurantApiType.ZOMATO)
)

fun mapStringToRestaurantType(string: String): RestaurantApiType? = enumMapper[string.toLowerCase()]