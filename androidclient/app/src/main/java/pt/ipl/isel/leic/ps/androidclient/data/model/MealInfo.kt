package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import pt.ipl.isel.leic.ps.androidclient.util.readTimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.writeTimestampWithTimeZone

open class MealInfo : MealItem {

    //TODO replace var to val when fields are not changed
    var submissionOwner: SubmissionOwner?
    var creationDate: TimestampWithTimeZone?
    var ingredientComponents: List<MealIngredient>
    var mealComponents: List<MealIngredient>
    var cuisines: List<Cuisine>
    var portions: Portions?

    constructor(
        dbId: Long?,
        dbRestaurantId: Long?,
        submissionId: Int?,
        restaurantSubmissionId: String?,
        name: String,
        carbs: Int,
        amount: Float,
        unit: WeightUnits,
        votes: Votes?,
        favorites: Favorites,
        imageUri: Uri?,
        creationDate: TimestampWithTimeZone?,
        ingredientComponents: List<MealIngredient>,
        mealComponents: List<MealIngredient>,
        cuisines: List<Cuisine>,
        portions: Portions?,
        isVerified: Boolean?,
        isSuggested: Boolean?,
        isReportable: Boolean?,
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
        favorites = favorites,
        imageUri = imageUri,
        isVerified = isVerified,
        isSuggested = isSuggested,
        isReportable = isReportable,
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
        this.portions = parcel.readParcelable(Portions::class.java.classLoader)!!
        this.submissionOwner = parcel.readParcelable(SubmissionOwner::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTimestampWithTimeZone(creationDate)
        parcel.writeList(ingredientComponents)
        parcel.writeList(mealComponents)
        parcel.writeList(cuisines)
        parcel.writeParcelable(portions, flags)
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