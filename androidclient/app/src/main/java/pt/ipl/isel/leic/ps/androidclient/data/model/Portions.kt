package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat

data class Portions(
    var dbId: Long,
    var dbMealId: Long,
    val userPortion: Float?,
    val allPortions: List<Float>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        dbMealId = parcel.readLong(),
        userPortion = parcel.readSerializable() as Float?,
        allPortions = parcel.readListCompat(Float::class)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeLong(dbMealId)
        parcel.writeSerializable(userPortion)
        parcel.writeList(allPortions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Portions> {
        override fun createFromParcel(parcel: Parcel) = Portions(parcel)

        override fun newArray(size: Int): Array<Portions?> {
            return arrayOfNulls(size)
        }
    }
}
