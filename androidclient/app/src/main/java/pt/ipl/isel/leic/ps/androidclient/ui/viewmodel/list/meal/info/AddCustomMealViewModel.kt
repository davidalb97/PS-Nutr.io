package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.util.readWeightUnit
import pt.ipl.isel.leic.ps.androidclient.util.writeWeightUnit

class AddCustomMealViewModel: MealInfoViewModel {

    var currentName: String? = null
    var currentAdditionalAmount: Float = 0.0F
    var currentWeightUnits: WeightUnits = DEFAULT_WEIGHT_UNIT
    var currentImg: String? = null

    constructor(): super()

    constructor(parcel: Parcel) : super(parcel) {
        currentName = parcel.readString()
        currentAdditionalAmount = parcel.readFloat()
        currentWeightUnits = parcel.readWeightUnit()
        currentImg = parcel.readString()
    }

    constructor(mealInfo: MealInfo) : super(
        mealInfo = mealInfo,
        ingredientActions = emptyList()
   )

    constructor(mealItem: MealItem) : super(
        mealItem = mealItem,
        ingredientActions = emptyList()
    )

    override fun update() {
        if(mealItem != null) {
            super.update()
        }
    }

    fun addCustomMeal(customMeal: CustomMeal, error: (Throwable) -> Unit, success: () -> Unit) =
        mealRepository.addCustomMeal(
            customMeal = customMeal,
            userSession = requireUserSession(),
            success = success,
            error = error
        )

    fun editCustomMeal(
        submission: Int,
        customMeal: CustomMeal,
        error: (Throwable) -> Unit,
        success: () -> Unit
    ) =
        mealRepository.editCustomMeal(
            submissionId = submission,
            customMeal = customMeal,
            userSession = requireUserSession(),
            success = success,
            error = error
        )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeString(currentName)
        dest?.writeFloat(currentAdditionalAmount)
        dest?.writeWeightUnit(currentWeightUnits)
        dest?.writeWeightUnit(currentWeightUnits)
        dest?.writeString(currentImg)
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