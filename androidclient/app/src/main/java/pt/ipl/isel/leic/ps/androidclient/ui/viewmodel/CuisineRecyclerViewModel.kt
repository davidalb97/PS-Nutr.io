package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.cuisineRepository
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.COUNT

class CuisineRecyclerViewModel() : ARecyclerViewModel<Cuisine>() {

    constructor(parcel: Parcel) : this() {
    }

    fun getCuisines(
        onSuccess : (List<Cuisine>) -> Unit,
        onError : () -> Unit
    ) {
        cuisineRepository.getCuisines(
            onSuccess,
            onError,
            parameters,
            COUNT,
            skip
        )
    }


    override fun fetchLiveData(): LiveData<List<Cuisine>> {
        TODO()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    companion object CREATOR : Parcelable.Creator<CuisineRecyclerViewModel> {

        override fun createFromParcel(parcel: Parcel): CuisineRecyclerViewModel {
            return CuisineRecyclerViewModel(parcel)
        }
        override fun newArray(size: Int): Array<CuisineRecyclerViewModel?> {
            return arrayOfNulls(size)
        }

    }


}
