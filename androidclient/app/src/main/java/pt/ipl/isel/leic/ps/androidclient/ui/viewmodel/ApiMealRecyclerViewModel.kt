package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem

class ApiMealRecyclerViewModel(val restaurantIdentifier: Int) : ARecyclerViewModel<MealItem>() {

    constructor(parcel: Parcel) : this(
        parcel.readInt()
    )

    override fun update() {
        mealRepository.getAllApiMealsByRestaurant(
            restaurantIdentifier,
            liveDataHandler::set,
            onError,
            parameters,
            count,
            skip
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    companion object CREATOR : Parcelable.Creator<ApiMealRecyclerViewModel> {

        override fun createFromParcel(parcel: Parcel): ApiMealRecyclerViewModel {
            return ApiMealRecyclerViewModel(parcel)
        }
        override fun newArray(size: Int): Array<ApiMealRecyclerViewModel?> {
            return arrayOfNulls(size)
        }

    }

}