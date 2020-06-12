package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Restaurant(
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Votes?,
    val isFavorite: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString()!!,
        name = parcel.readString()!!,
        latitude = parcel.readFloat(),
        longitude = parcel.readFloat(),
        votes = parcel.readParcelable<Votes>(Votes::class.java.classLoader)!!,
        isFavorite = parcel.readBoolean()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        //Does not need to close resources, using flag 0
        parcel.writeParcelable(votes, 0)
        parcel.writeBoolean(isFavorite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Restaurant> {
        override fun createFromParcel(parcel: Parcel): Restaurant {
            return Restaurant(parcel)
        }

        override fun newArray(size: Int): Array<Restaurant?> {
            return arrayOfNulls(size)
        }
    }
}