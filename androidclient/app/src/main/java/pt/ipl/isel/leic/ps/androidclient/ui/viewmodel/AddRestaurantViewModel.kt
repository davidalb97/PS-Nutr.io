package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomRestaurant
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession

class AddRestaurantViewModel(
    var editRestaurant: RestaurantInfo? = null
) : ViewModel(), Parcelable {

    constructor(parcel: Parcel) : this() {
        editRestaurant = parcel.readParcelable(RestaurantInfo::class.java.classLoader)
    }

    fun addRestaurant(customRestaurant: CustomRestaurant, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        restaurantRepository.addCustomRestaurant(
            customRestaurant = customRestaurant,
            onSuccess = onSuccess,
            onError = onError,
            userSession = requireUserSession()
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(editRestaurant, flags)
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<AddRestaurantViewModel> {
        override fun createFromParcel(parcel: Parcel): AddRestaurantViewModel {
            return AddRestaurantViewModel(parcel)
        }

        override fun newArray(size: Int): Array<AddRestaurantViewModel?> {
            return arrayOfNulls(size)
        }
    }
}