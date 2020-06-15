package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem

open class MealRecyclerViewModel() : ARecyclerViewModel<MealItem>() {

    lateinit var restaurantId: String

    constructor(parcel: Parcel): this()

    fun getSuggestedMeals() {
        mealRepository.getMealItems(
            success = { liveDataHandler.set(it) },
            error = onError
        )
    }

    override fun update() {
        mealRepository.getRestaurantMealItems(
            restaurantId = restaurantId!!,
            count = count,
            skip = skip,
            success = { liveDataHandler.set(it) },
            error = onError
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(restaurantId)
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    companion object CREATOR : Parcelable.Creator<MealRecyclerViewModel> {

        override fun createFromParcel(parcel: Parcel): MealRecyclerViewModel {
            return MealRecyclerViewModel(parcel)
        }
        override fun newArray(size: Int): Array<MealRecyclerViewModel?> {
            return arrayOfNulls(size)
        }

    }

}