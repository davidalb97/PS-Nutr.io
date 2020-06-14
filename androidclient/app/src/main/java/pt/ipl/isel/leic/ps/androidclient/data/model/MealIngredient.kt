package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.util.*

class MealIngredient(
    var dbId: Long,
    var dbMealId: Long,
    val submissionId: Int,
    val name: String,
    val imageUri: Uri?,
    val isFavorite: Boolean,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val isMeal: Boolean
): Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        dbMealId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readInt(),
        unit = parcel.readString()!!,
        isFavorite = readBoolean(parcel),
        imageUri = readUri(parcel),
        isMeal = readBoolean(parcel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeLong(dbMealId)
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        writeBoolean(parcel, isFavorite)
        writeUri(parcel, imageUri)
        writeBoolean(parcel, isMeal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealItem> {
        override fun createFromParcel(parcel: Parcel): MealItem {
            return MealItem(parcel)
        }

        override fun newArray(size: Int): Array<MealItem?> {
            return arrayOfNulls(size)
        }
    }
}