package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.util.*

class MealInfo(
    dbId: Long,
    dbRestaurantId: Long,
    submissionId: Int,
    restaurantSubmissionId: String?,
    name: String,
    override val carbs: Int,
    override val amount: Int,
    override val unit: String,
    votes: Votes?,
    isFavorite: Boolean,
    imageUri: Uri?,
    val creationDate: TimestampWithTimeZone?,
    val ingredientComponents: List<MealIngredient>,
    val mealComponents: List<MealIngredient>,
    val cuisines: List<Cuisine>,
    val portions: List<Portion>,
    isSuggested: Boolean,
    source: Source
): MealItem(
    dbId = dbId,
    dbRestaurantId = dbRestaurantId,
    submissionId = submissionId,
    restaurantSubmissionId = restaurantSubmissionId,
    name = name,
    carbs = carbs,
    amount = amount,
    unit = unit,
    votes = votes,
    isFavorite = isFavorite,
    imageUri = imageUri,
    isSuggested = isSuggested,
    source = source
) {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        dbRestaurantId = parcel.readLong(),
        submissionId = parcel.readInt(),
        restaurantSubmissionId = parcel.readString(),
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
        isSuggested = readBoolean(parcel),
        source = Source.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeLong(dbRestaurantId)
        parcel.writeInt(submissionId)
        parcel.writeString(restaurantSubmissionId)
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        parcel.writeParcelable(votes, flags)
        writeBoolean(parcel, isFavorite)
        writeUri(parcel, imageUri)
        writeTimestampWithTimeZone(parcel, creationDate)
        parcel.writeList(ingredientComponents)
        parcel.writeList(mealComponents)
        parcel.writeList(cuisines)
        parcel.writeList(portions)
        writeBoolean(parcel, isSuggested)
        parcel.writeInt(source.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealItem> {

        const val DEFAULT_SUBMISSION_ID: Int = -1

        override fun createFromParcel(parcel: Parcel): MealItem {
            return MealItem(parcel)
        }

        override fun newArray(size: Int): Array<MealItem?> {
            return arrayOfNulls(size)
        }
    }
}