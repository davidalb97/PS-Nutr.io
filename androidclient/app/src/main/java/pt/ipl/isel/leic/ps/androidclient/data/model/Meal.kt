package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Meal(
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?,
    val ingredients: List<Ingredient>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readInt(),
        unit = parcel.readString()!!,
        imageUrl = parcel.readString(),
        ingredients = ArrayList<Ingredient>().also {
            parcel.readList(it as List<Ingredient>, Ingredient::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        parcel.writeString(imageUrl)
        parcel.writeList(ingredients)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Meal> {
        override fun createFromParcel(parcel: Parcel): Meal {
            return Meal(parcel)
        }

        override fun newArray(size: Int): Array<Meal?> {
            return arrayOfNulls(size)
        }
    }
}