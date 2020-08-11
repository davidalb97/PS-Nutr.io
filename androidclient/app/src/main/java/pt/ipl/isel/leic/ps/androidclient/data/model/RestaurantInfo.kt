package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.util.*

data class RestaurantInfo(
    var dbId: Long,
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val creationDate: TimestampWithTimeZone?,
    val votes: Votes?,
    val isFavorite: Boolean,
    val imageUri: Uri?,
    val cuisines: List<Cuisine>,
    val meals: List<MealItem>,
    val suggestedMeals: List<MealItem>,
    val source: Source
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        id = parcel.readString()!!,
        name = parcel.readString()!!,
        latitude = parcel.readFloat(),
        longitude = parcel.readFloat(),
        creationDate = readTimestampWithTimeZone(parcel),
        votes = parcel.readParcelable(Votes::class.java.classLoader),
        isFavorite = parcel.readInt() != 0,
        imageUri = readUri(parcel),
        cuisines = readList(parcel, Cuisine::class),
        meals = readList(parcel, MealItem::class),
        suggestedMeals = readList(parcel, MealItem::class),
        source = Source.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeFloat(latitude)
        parcel.writeFloat(longitude)
        writeTimestampWithTimeZone(parcel, creationDate)
        parcel.writeParcelable(votes, flags)
        parcel.writeValue(isFavorite)
        writeUri(parcel, imageUri)
        parcel.writeList(cuisines)
        parcel.writeList(meals)
        parcel.writeList(suggestedMeals)
        parcel.writeInt(source.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantInfo> {
        override fun createFromParcel(parcel: Parcel): RestaurantInfo {
            return RestaurantInfo(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantInfo?> {
            return arrayOfNulls(size)
        }
    }
}