package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.util.getGlucoseUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.getWeightUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class AddInsulinProfileViewModel : ViewModel, Parcelable {

    var currentGlucoseUnit: GlucoseUnits
    var currentWeightUnit: WeightUnits
    var profileName: String?
    var startTime: String?
    var endTime: String?
    var glucoseObjective: Float?
    var glucoseReduction: Float?
    var carbReduction: Float?

    constructor() : super() {
        currentGlucoseUnit = sharedPreferences.getGlucoseUnitOrDefault()
        currentWeightUnit = sharedPreferences.getWeightUnitOrDefault()
        profileName = null
        startTime = null
        endTime = null
        glucoseObjective = null
        glucoseReduction = null
        carbReduction = null
    }

    constructor(parcel: Parcel) : super() {
        currentGlucoseUnit = GlucoseUnits.values()[parcel.readInt()]
        currentWeightUnit = WeightUnits.values()[parcel.readInt()]
        profileName = parcel.readString()
        startTime = parcel.readString()
        endTime = parcel.readString()
        glucoseObjective = parcel.readSerializable() as Float?
        glucoseReduction = parcel.readSerializable() as Float?
        carbReduction = parcel.readSerializable() as Float?
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(currentGlucoseUnit.ordinal)
        parcel.writeInt(currentWeightUnit.ordinal)
        parcel.writeString(profileName)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeSerializable(glucoseObjective)
        parcel.writeSerializable(glucoseReduction)
        parcel.writeSerializable(carbReduction)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddInsulinProfileViewModel> {
        override fun createFromParcel(parcel: Parcel) = AddInsulinProfileViewModel(parcel)

        override fun newArray(size: Int): Array<AddInsulinProfileViewModel?> {
            return arrayOfNulls(size)
        }
    }
}