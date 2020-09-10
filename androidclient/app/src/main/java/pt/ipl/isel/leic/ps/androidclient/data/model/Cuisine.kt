package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Cuisine(
    var dbId: Long,
    var dbMealId: Long,
    var dbRestaurantId: Long,
    val name: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        dbMealId = parcel.readLong(),
        dbRestaurantId = parcel.readLong(),
        name = parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeLong(dbMealId)
        parcel.writeLong(dbRestaurantId)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cuisine> {
        override fun createFromParcel(parcel: Parcel) = Cuisine(parcel)

        override fun newArray(size: Int): Array<Cuisine?> {
            return arrayOfNulls(size)
        }
    }
}
