package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbCustomMealDto

class CustomMealRecyclerViewModel() : ARecyclerViewModel<DbCustomMealDto>() {

    constructor(parcel: Parcel) : this() {
    }

    fun addCustomMeal(dbCustomMeal: DbCustomMealDto) =
        mealRepository.insertCustomMeal(dbCustomMeal)

    fun deleteItem(dbCustomMeal: DbCustomMealDto) =
        mealRepository.deleteCustomMeal(dbCustomMeal)

    override fun fetchLiveData(): LiveData<List<DbCustomMealDto>> =
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