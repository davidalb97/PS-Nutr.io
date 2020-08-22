package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.*

open class MealInfo : MealItem {

    final override val carbs: Int
    final override val amount: Int
    final override val unit: String
    val creationDate: TimestampWithTimeZone?
    var ingredientComponents: List<MealIngredient>
    var mealComponents: List<MealIngredient>
    val cuisines: List<Cuisine>
    val portions: List<Portion>

    constructor(
        dbId: Long?,
        dbRestaurantId: Long?,
        submissionId: Int?,
        restaurantSubmissionId: String?,
        name: String,
        carbs: Int,
        amount: Int,
        unit: String,
        votes: Votes?,
        isFavorite: Boolean,
        isVotable: Boolean,
        imageUri: Uri?,
        creationDate: TimestampWithTimeZone?,
        ingredientComponents: List<MealIngredient>,
        mealComponents: List<MealIngredient>,
        cuisines: List<Cuisine>,
        portions: List<Portion>,
        isSuggested: Boolean,
        source: Source
    ) : super(
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
        isVotable = isVotable,
        imageUri = imageUri,
        isSuggested = isSuggested,
        source = source
    ) {
        this.carbs = carbs
        this.amount = amount
        this.unit = unit
        this.creationDate = creationDate
        this.ingredientComponents = ingredientComponents
        this.mealComponents = mealComponents
        this.cuisines = cuisines
        this.portions = portions
    }

    constructor(parcel: Parcel) : super(parcel) {
        this.carbs = parcel.readInt()
        this.amount = parcel.readInt()
        this.unit = parcel.readString()!!
        this.creationDate = parcel.readTimestampWithTimeZone()
        this.ingredientComponents = parcel.readListCompat(MealIngredient::class)
        this.mealComponents = parcel.readListCompat(MealIngredient::class)
        this.cuisines = parcel.readListCompat(Cuisine::class)
        this.portions = parcel.readListCompat(Portion::class)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        parcel.writeTimestampWithTimeZone(creationDate)
        parcel.writeList(ingredientComponents)
        parcel.writeList(mealComponents)
        parcel.writeList(cuisines)
        parcel.writeList(portions)
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