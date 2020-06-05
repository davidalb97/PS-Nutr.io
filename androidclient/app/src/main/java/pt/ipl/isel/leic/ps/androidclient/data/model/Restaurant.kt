package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Restaurant(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: Votes,
    val cuisines: List<String>,
    val meals: List<ApiMeal>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString()!!,
        latitude = parcel.readFloat(),
        longitude = parcel.readFloat(),
        votes = parcel.readParcelable<Votes>(Votes::class.java.classLoader)!!,
        cuisines = ArrayList<String>().also {
            parcel.readList(it as List<String>, String::class.java.classLoader)
        },
        meals = ArrayList<ApiMeal>().also {
            parcel.readList(it as List<ApiMeal>, Meal::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        //Does not need to close resources, using flag 0
        parcel.writeParcelable(votes, 0)
        parcel.writeList(cuisines)
        parcel.writeList(meals)
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