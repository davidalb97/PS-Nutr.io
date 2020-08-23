package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import pt.ipl.isel.leic.ps.androidclient.util.readTimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.writeTimestampWithTimeZone

open class MealInfo : MealItem {

    val submissionOwner: SubmissionOwner?
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
        source: Source,
        submissionOwner: SubmissionOwner?
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
        this.creationDate = creationDate
        this.ingredientComponents = ingredientComponents
        this.mealComponents = mealComponents
        this.cuisines = cuisines
        this.portions = portions
        this.submissionOwner = submissionOwner
    }

    constructor(parcel: Parcel) : super(parcel) {
        this.creationDate = parcel.readTimestampWithTimeZone()
        this.ingredientComponents = parcel.readListCompat(MealIngredient::class)
        this.mealComponents = parcel.readListCompat(MealIngredient::class)
        this.cuisines = parcel.readListCompat(Cuisine::class)
        this.portions = parcel.readListCompat(Portion::class)
        this.submissionOwner = parcel.readParcelable(SubmissionOwner::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTimestampWithTimeZone(creationDate)
        parcel.writeList(ingredientComponents)
        parcel.writeList(mealComponents)
        parcel.writeList(cuisines)
        parcel.writeList(portions)
        parcel.writeParcelable(submissionOwner, flags)
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