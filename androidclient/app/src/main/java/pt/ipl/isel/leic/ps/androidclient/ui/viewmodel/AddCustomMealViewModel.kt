package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class AddCustomMealViewModel(
    var editMeal: MealInfo? = null
) : ViewModel(), Parcelable {

    var currentName: String? = null
    var currentAdditionalAmount: Float = 0.0F
    var currentWeightUnits: WeightUnits = DEFAULT_WEIGHT_UNIT
    var currentImg: String? = null

    constructor(parcel: Parcel) : this(
        editMeal = parcel.readParcelable(MealInfo::class.java.classLoader)
    )

    fun addCustomMeal(customMeal: CustomMeal, error: (Throwable) -> Unit, success: () -> Unit) =
        mealRepository.addCustomMeal(
            customMeal = customMeal,
            userSession = requireUserSession(),
            success = success,
            error = error
        )

    fun editCustomMeal(submission: Int, customMeal: CustomMeal, error: (Throwable) -> Unit, success: () -> Unit) =
        mealRepository.editCustomMeal(
            submissionId = submission,
            customMeal = customMeal,
            userSession = requireUserSession(),
            success = success,
            error = error
        )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(editMeal, flags)
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