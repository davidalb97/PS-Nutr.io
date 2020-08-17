package pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder

import android.content.Context
import android.view.View
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo

interface IMealItemDetail<T : MealInfo> {

    val customMealQuantity: TextView
    val customMealCarbs: TextView

    fun setupCustomMeal(context: Context, mealInfo: MealInfo) {
        val resources = context.resources
        customMealQuantity.text = String.format(
            resources.getString(R.string.meal_quantity_card),
            mealInfo.amount,
            mealInfo.unit
        )
        customMealQuantity.visibility = View.VISIBLE
        customMealCarbs.text = String.format(
            resources.getString(R.string.carbohydrates_amount_card),
            mealInfo.carbs
        )
        customMealCarbs.visibility = View.VISIBLE
    }
}