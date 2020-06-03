package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class FavoriteMeal(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val glucoseAmount: Int,
    val carbsAmount: Int,
    val ingredients: Iterable<Ingredient>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        TODO("ingredients")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeInt(glucoseAmount)
        parcel.writeInt(carbsAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteMeal> {
        override fun createFromParcel(parcel: Parcel): FavoriteMeal {
            return FavoriteMeal(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteMeal?> {
            return arrayOfNulls(size)
        }
    }
}