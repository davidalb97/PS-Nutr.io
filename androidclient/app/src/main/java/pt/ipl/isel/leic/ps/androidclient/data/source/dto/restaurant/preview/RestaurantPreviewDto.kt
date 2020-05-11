package pt.ipl.isel.leic.ps.androidclient.data.source.dto.restaurant.preview

class RestaurantPreviewDto(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Array<Boolean>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantPreviewDto

        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (!votes.contentEquals(other.votes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + votes.contentHashCode()
        return result
    }
}