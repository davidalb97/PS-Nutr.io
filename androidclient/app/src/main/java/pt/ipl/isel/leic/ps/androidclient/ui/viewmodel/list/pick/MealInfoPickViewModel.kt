package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import kotlin.reflect.KClass

class MealInfoPickViewModel : BaseItemPickerViewModel<MealInfo> {

    constructor(parcel: Parcel) : super(parcel)

    constructor() : super()

    override fun fetch() {
        //Unused
    }

    override fun getModelClass(): KClass<MealInfo> = MealInfo::class

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealInfoPickViewModel> {
        override fun createFromParcel(parcel: Parcel): MealInfoPickViewModel {
            return MealInfoPickViewModel(parcel)
        }

        override fun newArray(size: Int): Array<MealInfoPickViewModel?> {
            return arrayOfNulls(size)
        }
    }
}