package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem

class CustomMealRecyclerViewModel() : ARecyclerViewModel<MealItem>() {

    constructor(parcel: Parcel) : this() {
    }

    fun addCustomMeal(customMeal: MealInfo) =
        mealRepository.insert(customMeal)

    fun deleteItem(customMeal: MealItem) =
        mealRepository.deleteItem(customMeal)

    override fun update() {
        this.liveDataHandler.set(mealRepository.getAllMeals()) {
            mealRepository.dbMealInfoMapper.mapToModel(it)
        }
    }

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