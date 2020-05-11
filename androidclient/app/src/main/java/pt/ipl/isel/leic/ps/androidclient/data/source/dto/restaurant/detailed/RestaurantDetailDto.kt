package pt.ipl.isel.leic.ps.androidclient.data.source.dto.restaurant.detailed

import pt.ipl.isel.leic.ps.androidclient.data.source.dto.cuisine.preview.CuisineDto
import pt.ipl.isel.leic.ps.androidclient.data.source.dto.meal.preview.MealDto

class RestaurantDetailDto(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Array<Boolean>,
    val cuisines: Array<CuisineDto>,
    val meals: Array<MealDto>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantDetailDto

        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + votes.contentHashCode()
        result = 31 * result + cuisines.contentHashCode()
        result = 31 * result + meals.contentHashCode()
        return result
    }
}