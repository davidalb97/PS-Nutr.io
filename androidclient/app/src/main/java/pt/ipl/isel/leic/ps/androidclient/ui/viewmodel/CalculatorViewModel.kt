package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.util.getGlucoseUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.getWeightUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class CalculatorViewModel : ViewModel, Parcelable {

    var currentGlucoseUnit: GlucoseUnits
    var currentWeightUnit: WeightUnits
    var currentBloodGlucose: Float? = null

    constructor() : super() {
        currentGlucoseUnit = sharedPreferences.getGlucoseUnitOrDefault()
        currentWeightUnit = sharedPreferences.getWeightUnitOrDefault()
    }

    constructor(parcel: Parcel) : super() {
        currentGlucoseUnit = GlucoseUnits.values()[parcel.readInt()]
        currentWeightUnit = WeightUnits.values()[parcel.readInt()]
        currentBloodGlucose = parcel.readSerializable() as Float?
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(currentGlucoseUnit.ordinal)
        parcel.writeInt(currentWeightUnit.ordinal)
        parcel.writeSerializable(currentBloodGlucose)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CalculatorViewModel> {
        override fun createFromParcel(parcel: Parcel) = CalculatorViewModel(parcel)

        override fun newArray(size: Int): Array<CalculatorViewModel?> {
            return arrayOfNulls(size)
        }
    }
}