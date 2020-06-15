package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo

class RestaurantInfoMealRecyclerViewModel : MealRecyclerViewModel(), Parcelable {

    var restaurantInfo: RestaurantInfo? = null

    fun fetchInfo(onSuccess: (RestaurantInfo) -> Unit, onError: (Throwable) -> Unit) {
        restaurantRepository.getRestaurantInfoById(
            restaurantId,
            onSuccess,
            onError
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
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