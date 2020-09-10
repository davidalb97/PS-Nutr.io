package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.readTimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.writeTimestampWithTimeZone
import java.text.SimpleDateFormat

//private val timeFormat = SimpleDateFormat("HH:MM")

data class InsulinProfile(
    @PrimaryKey val profileName: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Float,
    val glucoseAmountPerInsulin: Float,
    val carbsAmountPerInsulin: Float,
    val modificationDate: TimestampWithTimeZone?
) : Parcelable {

    private val startInt by lazy { startTime.replace(":", "").toInt() }
    private val endInt by lazy { endTime.replace(":", "").toInt() }

    constructor(parcel: Parcel) : this(
        profileName = parcel.readString()!!,
        modificationDate = parcel.readTimestampWithTimeZone(),
        startTime = parcel.readString()!!,
        endTime = parcel.readString()!!,
        glucoseObjective = parcel.readFloat(),
        glucoseAmountPerInsulin = parcel.readFloat(),
        carbsAmountPerInsulin = parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profileName)
        parcel.writeTimestampWithTimeZone(modificationDate)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeFloat(glucoseObjective)
        parcel.writeFloat(glucoseAmountPerInsulin)
        parcel.writeFloat(carbsAmountPerInsulin)
    }

    //TODO replace with a cleaner solution is found
    fun isValid(): Boolean {
        return startInt < endInt
    }

    //TODO replace with a cleaner solution is found
    fun overlaps(oldProfile: InsulinProfile): Boolean {
        return oldProfile.endInt > startInt && oldProfile.startInt < endInt
    }

    //TODO remove if no other solution is found
    /*
    fun overlapsNotWorking(oldProfile: InsulinProfile): Boolean {
        val newProfileStartTime = timeFormat.parse(startTime)!!
        val newProfileEndTime = timeFormat.parse(endTime)!!
        val oldProfileSavedStartTime = timeFormat.parse(oldProfile.startTime)!!
        val oldProfileSavedEndTime = timeFormat.parse(oldProfile.endTime)!!

        return oldProfileSavedEndTime.after(newProfileStartTime)
                && oldProfileSavedStartTime.before(newProfileEndTime)
    }
     */

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsulinProfile> {
        override fun createFromParcel(parcel: Parcel) = InsulinProfile(parcel)

        override fun newArray(size: Int): Array<InsulinProfile?> {
            return arrayOfNulls(size)
        }
    }
}
