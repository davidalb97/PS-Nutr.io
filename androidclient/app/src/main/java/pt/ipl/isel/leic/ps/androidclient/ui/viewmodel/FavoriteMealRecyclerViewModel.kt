package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMeal

class FavoriteMealRecyclerViewModel() : ARecyclerViewModel<DbFavoriteMeal>() {

    constructor(parcel: Parcel) : this() {
    }

    override fun fetchLiveData(): LiveData<List<DbFavoriteMeal>> {
        TODO("Not yet implemented")
    }

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
}