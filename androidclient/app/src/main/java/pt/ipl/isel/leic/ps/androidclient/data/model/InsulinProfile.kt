package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

data class InsulinProfile(
    @PrimaryKey val profileName: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Int,
    val glucoseAmountPerInsulin: Int,
    val carbsAmountPerInsulin: Int
): Parcelable {

    constructor(parcel: Parcel) : this(
        profileName = parcel.readString()!!,
        startTime = parcel.readString()!!,
        endTime = parcel.readString()!!,
        glucoseObjective = parcel.readInt(),
        glucoseAmountPerInsulin = parcel.readInt(),
        carbsAmountPerInsulin = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profileName)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeInt(glucoseObjective)
        parcel.writeInt(glucoseAmountPerInsulin)
        parcel.writeInt(carbsAmountPerInsulin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsulinProfile> {
        override fun createFromParcel(parcel: Parcel): InsulinProfile {
            return InsulinProfile(parcel)
        }

        override fun newArray(size: Int): Array<InsulinProfile?> {
            return arrayOfNulls(size)
        }
    }
}