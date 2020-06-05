package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Ingredient(
    val dbId: Long = 0,
    val dbMealId: Long = 0,
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?
): Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        dbMealId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readInt(),
        unit = parcel.readString()!!,
        imageUrl = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeLong(dbMealId)
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient {
            return Ingredient(parcel)
        }

        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }
    }
}