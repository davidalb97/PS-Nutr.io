package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Portion(
    var dbId: Long,
    var dbMealId: Long,
    val amount: Float
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        dbMealId = parcel.readLong(),
        amount = parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeLong(dbMealId)
        parcel.writeFloat(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Portion> {
        override fun createFromParcel(parcel: Parcel): Portion {
            return Portion(parcel)
        }

        override fun newArray(size: Int): Array<Portion?> {
            return arrayOfNulls(size)
        }
    }
}
