package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.util.readBoolean
import pt.ipl.isel.leic.ps.androidclient.data.util.readUri
import pt.ipl.isel.leic.ps.androidclient.data.util.writeBoolean
import pt.ipl.isel.leic.ps.androidclient.data.util.writeUri

class MealIngredient(
    dbId: Long,
    var dbMealId: Long,
    submissionId: Int,
    name: String,
    imageUri: Uri?,
    isFavorite: Boolean,
    carbs: Int,
    amount: Int,
    unit: String,
    val isMeal: Boolean
) : MealItem(
    dbId = dbId,
    dbRestaurantId = DbMealItemEntity.DEFAULT_DB_ID,
    submissionId = submissionId,
    restaurantSubmissionId = null,
    name = name,
    imageUri = imageUri,
    votes = null,
    isFavorite = isFavorite,
    carbs = carbs,
    amount = amount,
    unit = unit,
    isSuggested = false,
    source = Source.API
), Parcelable {

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
        parcel.writeInt(carbs!!)
        parcel.writeInt(amount!!)
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