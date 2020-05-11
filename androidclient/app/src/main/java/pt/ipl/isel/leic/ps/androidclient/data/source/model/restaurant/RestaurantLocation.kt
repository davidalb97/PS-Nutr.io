package pt.ipl.isel.leic.ps.androidclient.data.source.model.restaurant

import pt.ipl.isel.leic.ps.androidclient.data.source.model.restaurant.ARestaurant

data class RestaurantLocation(
    override val id : String?,
    override var name : String?,
    val latitude : Float?,
    val longitude : Float?
) : ARestaurant(id, name) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}