package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source

class FavoriteMealRecyclerViewModel() : ARecyclerViewModel<MealItem>() {

    constructor(parcel: Parcel) : this() {
    }

    fun deleteItem(favoriteMeal: MealItem) =
        mealRepository.deleteItem(favoriteMeal)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteMealRecyclerViewModel> {
        override fun createFromParcel(parcel: Parcel): FavoriteMealRecyclerViewModel {
            return FavoriteMealRecyclerViewModel(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteMealRecyclerViewModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun update() {
        //TODO filter by favorite FavoriteMealRecyclerViewModel.update()
        this.liveDataHandler.set(mealRepository.getAllInfoBySource(Source.FAVORITE)) {
            mealRepository.dbMealInfoMapper.mapToModel(it)
        }
    }
}