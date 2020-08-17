package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.readUri
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeUri

data class RestaurantItem(
    var dbId: Long,
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Votes?,
    var isFavorite: Boolean,
    val imageUri: Uri?,
    val source: Source
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        id = parcel.readString()!!,
        name = parcel.readString()!!,
        latitude = parcel.readFloat(),
        longitude = parcel.readFloat(),
        votes = parcel.readParcelable<Votes>(Votes::class.java.classLoader),
        isFavorite = parcel.readBooleanCompat(),
        imageUri = parcel.readUri(),
        source = Source.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeFloat(latitude)
        parcel.writeFloat(longitude)
        parcel.writeParcelable(votes, flags)
        parcel.writeBooleanCompat(isFavorite)
        parcel.writeUri(imageUri)
        parcel.writeInt(source.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantItem> {
        override fun createFromParcel(parcel: Parcel): RestaurantItem {
            return RestaurantItem(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantItem?> {
            return arrayOfNulls(size)
        }
    }
}