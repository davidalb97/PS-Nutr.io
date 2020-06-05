package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.COUNT

class RestaurantRecyclerViewModel : ARecyclerViewModel<Restaurant>() {

    // TODO - not for recycler
    /*fun getRestaurantById() {
        val successFunction : (Restaurant) -> Unit = {
            updateList(it)
            onSuccess(it)
        }
        restaurantRepository.getRestaurantById(
            successFunction,
            onError,
            parameters,
            COUNT,
            skip
        )
    }*/

    override fun update() {
        restaurantRepository.getNearbyRestaurants(
            liveDataHandler::add,
            onError,
            parameters,
            COUNT,
            skip
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
