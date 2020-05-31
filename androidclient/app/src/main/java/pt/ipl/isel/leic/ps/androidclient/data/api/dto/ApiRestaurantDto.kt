package pt.ipl.isel.leic.ps.androidclient.data.api.dto

class ApiRestaurantDto (
    val id: Int,
    val name: String,
    val latitude: Float?,
    val longitude: Float?,
    val votes: List<Boolean>?,
    val cuisines: List<String>?,
    val apiMeals: List<ApiMealDto>?
) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}