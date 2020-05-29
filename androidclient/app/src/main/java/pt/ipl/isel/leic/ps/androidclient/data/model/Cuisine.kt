package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

// TODO - add support for Room
data class Cuisine(val name: String) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cuisine> {
        override fun createFromParcel(parcel: Parcel): Cuisine {
            return Cuisine(parcel)
        }

        override fun newArray(size: Int): Array<Cuisine?> {
            return arrayOfNulls(size)
        }
    }
}
