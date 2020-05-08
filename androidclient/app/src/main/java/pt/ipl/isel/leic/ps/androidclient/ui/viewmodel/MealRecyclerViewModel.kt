package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.COUNT

class MealRecyclerViewModel() : ARecyclerViewModel<Meal>() {

    constructor(parcel: Parcel) : this() {
    }

    fun getMeals(
        onSuccess: (List<Meal>) -> Unit,
        onError: () -> Unit
    ) {
        mealRepository.getMeals(
            onSuccess,
            onError,
            parameters,
            COUNT,
            skip
        )
    }

    override fun fetchLiveData(): LiveData<List<Meal>> {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
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