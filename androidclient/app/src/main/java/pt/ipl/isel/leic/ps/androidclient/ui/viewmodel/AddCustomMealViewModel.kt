package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat

class AddCustomMealViewModel(
    var editMeal: MealInfo? = null,
    var addedIngredients: List<MealIngredient>? = null
) : ViewModel(), Parcelable {

    constructor(parcel: Parcel) : this(
        editMeal = parcel.readParcelable(MealInfo::class.java.classLoader),
        addedIngredients = parcel.readListCompat(MealIngredient::class)
    )

    fun addCustomMeal(customMeal: CustomMeal, error: (Throwable) -> Unit) =
        mealRepository.addCustomMeal(
            customMeal = customMeal,
            userSession = requireUserSession(),
            error = error
        )

    fun editCustomMeal(submission: Int, customMeal: CustomMeal, error: (Throwable) -> Unit) =
        mealRepository.editCustomMeal(
            submissionId = submission,
            customMeal = customMeal,
            userSession = requireUserSession(),
            error = error
        )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(editMeal, flags)
        dest?.writeList(addedIngredients)
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