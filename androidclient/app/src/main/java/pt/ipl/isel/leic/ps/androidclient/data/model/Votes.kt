package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Votes(
    val positive: Int,
    val negative: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        positive = parcel.readInt(),
        negative = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(positive)
        parcel.writeInt(negative)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Votes> {
        override fun createFromParcel(parcel: Parcel): Votes {
            return Votes(parcel)
        }

        override fun newArray(size: Int): Array<Votes?> {
            return arrayOfNulls(size)
        }
    }
}