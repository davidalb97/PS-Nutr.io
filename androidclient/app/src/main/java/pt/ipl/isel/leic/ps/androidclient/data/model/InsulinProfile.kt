package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.data.util.readTimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.data.util.writeTimestampWithTimeZone
import java.sql.Date

data class InsulinProfile(
    @PrimaryKey val profileName: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Int,
    val glucoseAmountPerInsulin: Int,
    val carbsAmountPerInsulin: Int,
    val modificationDate: TimestampWithTimeZone
): Parcelable {

    constructor(parcel: Parcel) : this(
        profileName = parcel.readString()!!,
        modificationDate = readTimestampWithTimeZone(parcel)!!,
        startTime = parcel.readString()!!,
        endTime = parcel.readString()!!,
        glucoseObjective = parcel.readInt(),
        glucoseAmountPerInsulin = parcel.readInt(),
        carbsAmountPerInsulin = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profileName)
        writeTimestampWithTimeZone(parcel, modificationDate)
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