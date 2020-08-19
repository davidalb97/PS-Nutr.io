package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.readUri
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeUri

class MealIngredient(
    var dbMealId: Long,
    val isMeal: Boolean,
    dbId: Long,
    submissionId: Int,
    name: String,
    carbs: Int,
    amount: Int,
    unit: String,
    imageUri: Uri?,
    source: Source
) : MealInfo(
    dbId = dbId,
    dbRestaurantId = DbMealItemEntity.DEFAULT_DB_ID,
    submissionId = submissionId,
    restaurantSubmissionId = null,
    name = name,
    carbs = carbs,
    amount = amount,
    unit = unit,
    votes = null,
    isFavorite = false,
    //MealIngredients are never votable
    isVotable = false,
    imageUri = imageUri,
    creationDate = null,
    ingredientComponents = emptyList(),
    mealComponents = emptyList(),
    cuisines = emptyList(),
    portions = emptyList(),
    isSuggested = false,
    source = source
), Parcelable {

    constructor(parcel: Parcel) : this(
        dbMealId = parcel.readLong(),
        isMeal = parcel.readBooleanCompat(),
        dbId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readInt(),
        unit = parcel.readString()!!,
        imageUri = parcel.readUri(),
        source = Source.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbMealId)
        parcel.writeBooleanCompat(isMeal)
        parcel.writeLong(dbId)
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        parcel.writeUri(imageUri)
        parcel.writeInt(source.ordinal)
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