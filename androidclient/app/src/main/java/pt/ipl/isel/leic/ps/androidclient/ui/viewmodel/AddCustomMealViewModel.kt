package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

class AddCustomMealViewModel(
    var editMeal: MealInfo? = null,
    var addedIngredient: MealInfo? = null
) : ViewModel(), Parcelable {

    var dbId: Long? = null
    var dbRestaurantId: Long? = null
    var submissionId: Int? = null
    var restaurantSubmissionId: String? = null
    var name: String? = null
    var carbs: Int? = null
    var amount: Int? = null
    var unit: String? = null
    var imageUri: Uri? = null
    var ingredientComponents: List<MealIngredient>? = null
    var mealComponents: List<MealIngredient>? = null
    val cuisines: List<Cuisine>? = null

    constructor(parcel: Parcel) : this(
        addedIngredient = parcel.readParcelable(MealInfo::class.java.classLoader)
    )

    fun addCustomMeal(customMeal: MealInfo) =
        mealRepository.insertInfo(customMeal)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(addedIngredient, flags)
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<AddCustomMealViewModel> {
        override fun createFromParcel(parcel: Parcel): AddCustomMealViewModel {
            return AddCustomMealViewModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<AddCustomMealViewModel?> {
            return arrayOfNulls(size)
        }
    }

}