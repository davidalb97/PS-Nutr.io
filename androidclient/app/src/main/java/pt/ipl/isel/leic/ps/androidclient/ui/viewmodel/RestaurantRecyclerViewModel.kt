package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.COUNT

class RestaurantRecyclerViewModel : ARecyclerViewModel<RestaurantItem>() {

    lateinit var restaurantId: String
    var latitude: Double? = null
    var longitude: Double? = null

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
        TODO()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantRecyclerViewModel> {
        override fun createFromParcel(parcel: Parcel): RestaurantRecyclerViewModel =
            TODO()


        override fun newArray(size: Int): Array<RestaurantRecyclerViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
