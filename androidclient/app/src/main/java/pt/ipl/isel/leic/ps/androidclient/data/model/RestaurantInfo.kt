package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import java.time.OffsetDateTime

class RestaurantInfo(
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Votes?,
    val isFavorite: Boolean?,
    val cuisines: Collection<String>,
    val creationDate: OffsetDateTime,
    val meals: Collection<MealInfo>,
    val suggestedMeals: Collection<MealInfo>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        TODO("votes"),
        parcel.readString(),
        TODO("ingredients")
    ) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
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