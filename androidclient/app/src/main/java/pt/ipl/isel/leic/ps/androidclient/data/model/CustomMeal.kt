package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class CustomMeal(
    val name: String,
    val mealQuantity: Int,
    val glucoseAmount: Int,
    val carboAmount: Int,
    val ingredients: Iterable<Ingredient>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        TODO("ingredients")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(mealQuantity)
        parcel.writeInt(glucoseAmount)
        parcel.writeInt(carboAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomMeal> {
        override fun createFromParcel(parcel: Parcel): CustomMeal {
            return CustomMeal(parcel)
        }

        override fun newArray(size: Int): Array<CustomMeal?> {
            return arrayOfNulls(size)
        }
    }
}