package pt.isel.ps.g06.httpserver.dto

data class RestaurantContainer(val restaurants: Array<RestaurantDto>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantContainer

        if (!restaurants.contentEquals(other.restaurants)) return false

        return true
    }

    override fun hashCode(): Int {
        return restaurants.contentHashCode()
    }
}

data class RestaurantDto(val id: Int?, val name: String?, val url: String?, val cuisines: CuisinesDto?) {

    override fun toString() = "id=$id, name=$name, cuisines=$cuisines, url=$url"
}

