package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.cuisineRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import kotlin.reflect.KClass

private val ITEM_CLASS = Cuisine::class

class CuisinePickViewModel : BaseItemPickerViewModel<Cuisine> {

    constructor(parcel: Parcel) : super(parcel, ITEM_CLASS)

    constructor() : super(ITEM_CLASS)

    init {
        skip = null
        count = null
    }

    override fun fetch() {
        cuisineRepository.getCuisines(count, skip, liveDataHandler::set, onError)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CuisinePickViewModel> {
        override fun createFromParcel(parcel: Parcel) = CuisinePickViewModel(parcel)

        override fun newArray(size: Int): Array<CuisinePickViewModel?> {
            return arrayOfNulls(size)
        }
    }
}