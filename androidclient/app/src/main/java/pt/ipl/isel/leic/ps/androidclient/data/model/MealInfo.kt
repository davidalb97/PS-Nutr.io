package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.util.*

class MealInfo(
    var dbId: Long,
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val votes: Votes?,
    val isFavorite: Boolean,
    val imageUri: Uri?,
    val creationDate: TimestampWithTimeZone?,
    val ingredientComponents: List<MealIngredient>,
    val mealComponents: List<MealIngredient>,
    val cuisines: List<Cuisine>,
    val portions: List<Portion>,
    val isSuggested: Boolean
): Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readInt(),
        unit = parcel.readString()!!,
        votes = parcel.readParcelable(Votes::class.java.classLoader),
        isFavorite = readBoolean(parcel),
        imageUri = readUri(parcel),
        creationDate = readTimestampWithTimeZone(parcel),
        ingredientComponents = readList(parcel, MealIngredient::class),
        mealComponents = readList(parcel, MealIngredient::class),
        cuisines = readList(parcel, Cuisine::class),
        portions = readList(parcel, Portion::class),
        isSuggested = readBoolean(parcel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        parcel.writeParcelable(votes, flags)
        writeBoolean(parcel, isFavorite)
        writeUri(parcel, imageUri)
        parcel.writeList(ingredientComponents)
        parcel.writeList(mealComponents)
        parcel.writeList(cuisines)
        parcel.writeList(portions)
        writeBoolean(parcel, isSuggested)
        writeTimestampWithTimeZone(parcel, creationDate)
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