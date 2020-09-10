package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.readUri
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeUri

open class RestaurantItem(
    var dbId: Long?,
    val id: String?,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Votes,
    var favorites: Favorites,
    val isReportable: Boolean,
    val image: Uri?,
    val source: Source
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readSerializable() as Long?,
        id = parcel.readString(),
        name = parcel.readString()!!,
        latitude = parcel.readFloat(),
        longitude = parcel.readFloat(),
        votes = parcel.readParcelable<Votes>(Votes::class.java.classLoader)!!,
        favorites = parcel.readParcelable<Favorites>(Favorites::class.java.classLoader)!!,
        isReportable = parcel.readBooleanCompat(),
        image = parcel.readUri(),
        source = Source.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(dbId)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeFloat(latitude)
        parcel.writeFloat(longitude)
        parcel.writeParcelable(votes, flags)
        parcel.writeParcelable(favorites, flags)
        parcel.writeBooleanCompat(isReportable)
        parcel.writeUri(image)
        parcel.writeInt(source.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantItem> {
        override fun createFromParcel(parcel: Parcel) = RestaurantItem(parcel)

        override fun newArray(size: Int): Array<RestaurantItem?> {
            return arrayOfNulls(size)
        }
    }
}