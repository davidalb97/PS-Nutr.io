package pt.isel.ps.g06.httpserver.dataAccess.input

data class RestaurantInput(
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val cuisines: Array<String>,
        val submitterId: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantInput

        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + cuisines.contentHashCode()
        return result
    }
}