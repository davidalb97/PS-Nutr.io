package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto

class CustomMealRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ADeletableRecyclerViewHolder<CustomMealDto>(view, ctx){

    private val customMealName: TextView =
        view.findViewById(R.id.custom_meal_name)
    private val customMealQuantity: TextView =
        view.findViewById(R.id.custom_meal_quantity)
    private val customMealGlucose: TextView =
        view.findViewById(R.id.custom_meal_glucose_amount)
    private val customMealCarbs: TextView =
        view.findViewById(R.id.custom_meal_carbs_amount)
    private val customMealDelete: ImageButton =
        view.findViewById(R.id.delete_item_button)
    private val customMealAddToCalc: ImageButton =
        view.findViewById(R.id.add_custom_meal_to_calc)

    override fun bindTo(item: CustomMealDto) {
        super.bindTo(item)
        setupViewHolderElements()
        setupListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViewHolderElements() {
        val resources = ctx.resources
        customMealName.text = item.name
        customMealQuantity.text =
            resources.getString(R.string.meal_quantity_card) +
                    " ${item.mealQuantity}"
        customMealGlucose.text =
            resources.getString(R.string.glucose_amount_card) +
                    " ${item.glucoseAmount}"
        customMealCarbs.text =
            resources.getString(R.string.carbohydrates_amount_card) +
                    " ${item.carboAmount}"
        this.deleteButton = customMealDelete
        this.addToCalculatorButton = customMealAddToCalc
    }

    private fun setupListeners() {
        this.view.setOnClickListener(this)
        this.view.setOnLongClickListener(this)
    }

}