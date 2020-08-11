package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem

class RestaurantRecyclerViewModel : ARecyclerViewModel<RestaurantItem>() {

    var restaurantName: String? = null
    var latitude: Double? = null
    var longitude: Double? = null

    fun addRestaurantVote() {

    }

    fun deleteRestaurantVote() {

    }

    override fun update() {
        restaurantRepository.getNearbyRestaurants(
            latitude!!,
            longitude!!,
            count,
            skip,
            liveDataHandler::add,
            onError
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        TODO("Save RestaurantRecyclerViewModel to bundle")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantRecyclerViewModel> {
        override fun createFromParcel(parcel: Parcel): RestaurantRecyclerViewModel =
            TODO("Restore RestaurantRecyclerViewModel from bundle")


        override fun newArray(size: Int): Array<RestaurantRecyclerViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
