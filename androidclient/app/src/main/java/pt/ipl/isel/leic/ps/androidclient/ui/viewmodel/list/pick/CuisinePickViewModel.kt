package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.cuisineRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import kotlin.reflect.KClass

class CuisinePickViewModel : BaseItemPickerViewModel<Cuisine> {

    constructor(parcel: Parcel) : super(parcel)

    constructor() : super()

    init {
        skip = null
        count = null
    }

    override fun fetch() {
        if (!tryRestore()) {
            cuisineRepository.getCuisines(count, skip, liveDataHandler::set, onError)
        }
    }

    override fun getModelClass(): KClass<Cuisine> = Cuisine::class

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CuisinePickViewModel> {
        override fun createFromParcel(parcel: Parcel): CuisinePickViewModel {
            return CuisinePickViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CuisinePickViewModel?> {
            return arrayOfNulls(size)
        }
    }
}