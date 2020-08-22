package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class CustomMeal : MealInfo, Parcelable {

    constructor(
        dbId: Long?,
        submissionId: Int?,
        name: String,
        carbs: Int,
        amount: Int,
        unit: String,
        imageUri: Uri?
    ) : super(
        dbId = dbId,
        dbRestaurantId = null,
        submissionId = submissionId,
        restaurantSubmissionId = null,
        name = name,
        carbs = carbs,
        amount = amount,
        unit = unit,
        votes = null,
        isFavorite = false,
        isVotable = false,
        imageUri = imageUri,
        creationDate = null,
        ingredientComponents = emptyList(),
        mealComponents = emptyList(),
        cuisines = emptyList(),
        portions = emptyList(),
        isSuggested = false,
        source = Source.CUSTOM
    )

    constructor(parcel: Parcel) : super(parcel)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomMeal> {
        override fun createFromParcel(parcel: Parcel): CustomMeal {
            return CustomMeal(parcel)
        }

        override fun newArray(size: Int): Array<CustomMeal?> {
            return arrayOfNulls(size)
        }
    }
}