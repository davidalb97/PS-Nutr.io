package pt.ipl.isel.leic.ps.androidclient.data.source.model.restaurant

// TODO
data class Restaurant(
        val apiId: Int,
        val apiType: String,
        override val name: String,
        val votes: List<Boolean>,
        val latitude: Float,
        val longitude: Float,
        val cuisines: Array<String>
) : ARestaurant(apiType, name){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (apiId != other.apiId) return false
        if (apiType != other.apiType) return false
        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = apiId
        result = 31 * result + apiType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + cuisines.contentHashCode()
        return result
    }
}