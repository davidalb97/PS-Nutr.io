package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomRestaurant
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession

class AddRestaurantViewModel() : ViewModel(), Parcelable {

    var name: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
    }

    fun addRestaurant(
        customRestaurant: CustomRestaurant,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        restaurantRepository.addCustomRestaurant(
            customRestaurant = customRestaurant,
            onSuccess = onSuccess,
            onError = onError,
            userSession = requireUserSession()
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AddRestaurantViewModel> {
        override fun createFromParcel(parcel: Parcel) = AddRestaurantViewModel(parcel)

        override fun newArray(size: Int): Array<AddRestaurantViewModel?> {
            return arrayOfNulls(size)
        }
    }
}