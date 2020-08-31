package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class CustomMeal : MealInfo, Parcelable {

    constructor(
        dbId: Long?,
        submissionId: Int?,
        name: String,
        carbs: Int,
        amount: Float,
        unit: WeightUnits,
        imageUri: Uri?,
        ingredientComponents: List<MealIngredient>,
        mealComponents: List<MealIngredient>,
        cuisines: List<Cuisine>
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
        ingredientComponents = ingredientComponents,
        mealComponents = mealComponents,
        cuisines = cuisines,
        portions = emptyList(),
        isSuggested = false,
        source = Source.CUSTOM,
        //TODO pass owner to custom meal ctor
        submissionOwner = null
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