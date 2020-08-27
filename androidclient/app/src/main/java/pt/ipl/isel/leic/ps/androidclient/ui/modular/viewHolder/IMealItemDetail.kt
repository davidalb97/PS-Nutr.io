package pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder

import android.content.Context
import android.view.View
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.util.getWeightUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

interface IMealItemDetail<T : MealItem> {

    val customMealQuantityId: Int
    var customMealQuantity: TextView
    val customMealCarbsId: Int
    var customMealCarbs: TextView

    fun setupCustomMeal(view: View, context: Context, mealInfo: MealItem) {

        customMealQuantity = view.findViewById(customMealQuantityId)
        customMealCarbs = view.findViewById(customMealCarbsId)

        val resources = context.resources

        val configuredUnit = WeightUnits.fromValue(sharedPreferences.getWeightUnitOrDefault())
        customMealQuantity.text = String.format(
            resources.getString(R.string.meal_quantity_card),
            WeightUnits.fromValue(mealInfo.unit).convert(configuredUnit, mealInfo.amount.toFloat()),
            configuredUnit
        )

        customMealQuantity.visibility = View.VISIBLE
        customMealCarbs.text = String.format(
            resources.getString(R.string.carbohydrates_amount_card),
            mealInfo.carbs
        )
        customMealCarbs.visibility = View.VISIBLE
    }
}