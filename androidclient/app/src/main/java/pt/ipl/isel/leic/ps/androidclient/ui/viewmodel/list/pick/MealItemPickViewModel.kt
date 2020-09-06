package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import kotlin.reflect.KClass

class MealItemPickViewModel : BaseItemPickerViewModel<MealItem> {

    var itemsChanged = false

    constructor(parcel: Parcel) : super(parcel)

    constructor() : super()

    override fun fetch() {
        tryRestore()
    }

    override fun getModelClass(): KClass<MealItem> = MealItem::class

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealItemPickViewModel> {
        override fun createFromParcel(parcel: Parcel): MealItemPickViewModel {
            return MealItemPickViewModel(parcel)
        }

        override fun newArray(size: Int): Array<MealItemPickViewModel?> {
            return arrayOfNulls(size)
        }
    }
}