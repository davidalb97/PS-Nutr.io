package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import java.time.OffsetDateTime

data class RestaurantInfo(
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Votes?,
    val isFavorite: Boolean?,
    val cuisines: Collection<String>,
    val creationDate: OffsetDateTime,
    val meals: List<RestaurantMeal>,
    val suggestedMeals: List<RestaurantMeal>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readParcelable(Votes::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        TODO("cuisines"),
        TODO("creationDate"),
        TODO("meals"),
        TODO("suggestedMeals")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeFloat(latitude)
        parcel.writeFloat(longitude)
        parcel.writeParcelable(votes, flags)
        parcel.writeValue(isFavorite)
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