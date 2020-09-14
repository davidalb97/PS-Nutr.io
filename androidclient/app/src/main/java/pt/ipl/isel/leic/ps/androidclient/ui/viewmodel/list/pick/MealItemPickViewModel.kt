package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.util.getWeightUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

private val ITEM_CLASS = MealItem::class

class MealItemPickViewModel : BaseItemPickerViewModel<MealItem> {

    var itemsChanged = false
    var currentWeightUnits: WeightUnits = sharedPreferences.getWeightUnitOrDefault()

    constructor(parcel: Parcel) : super(parcel, ITEM_CLASS) {
        currentWeightUnits = WeightUnits.values()[parcel.readInt()]
    }

    constructor() : super(ITEM_CLASS)

    override fun fetch() {
        //Unused
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeInt(currentWeightUnits.ordinal)
    }

    companion object CREATOR : Parcelable.Creator<MealItemPickViewModel> {
        override fun createFromParcel(parcel: Parcel) = MealItemPickViewModel(parcel)

        override fun newArray(size: Int): Array<MealItemPickViewModel?> {
            return arrayOfNulls(size)
        }
    }
}