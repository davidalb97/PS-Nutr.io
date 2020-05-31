package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto

class CustomMealRecyclerViewModel() : ARecyclerViewModel<CustomMealDto>() {

    constructor(parcel: Parcel) : this() {
    }

    fun addCustomMeal(customMeal: CustomMealDto) =
        mealRepository.insertCustomMeal(customMeal)

    fun deleteItem(customMeal: CustomMealDto) =
        mealRepository.deleteCustomMeal(customMeal)

    override fun fetchLiveData(): LiveData<List<CustomMealDto>> =
        mealRepository.getAllCustomMeals()

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }


    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CustomMealRecyclerViewModel> {
        override fun createFromParcel(parcel: Parcel): CustomMealRecyclerViewModel {
            return CustomMealRecyclerViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CustomMealRecyclerViewModel?> {
            return arrayOfNulls(size)
        }
    }
}