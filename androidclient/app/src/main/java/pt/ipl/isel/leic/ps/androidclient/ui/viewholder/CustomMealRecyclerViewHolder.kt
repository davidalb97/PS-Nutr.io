package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto

class CustomMealRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ADeletableRecyclerViewHolder<CustomMealDto>(view, ctx) {

    private val customAddButton: ImageButton =
        view.findViewById(R.id.add_custom_meal_to_calc)
    private val customMealName: TextView =
        view.findViewById(R.id.custom_meal_name)
    private val customMealQuantity: TextView =
        view.findViewById(R.id.custom_meal_quantity)
    private val customMealGlucose: TextView =
        view.findViewById(R.id.custom_meal_glucose_amount)
    private val customMealCarbs: TextView =
        view.findViewById(R.id.custom_meal_carbs_amount)

    override fun bindTo(item: CustomMealDto) {
        super.bindTo(item)
        setupTextFields()
        setupListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setupTextFields() {
        val resources = ctx.resources
        if (needsAddButton) {
            customAddButton.visibility = View.VISIBLE
        }
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
    }

    private fun setupListeners() {
        this.view.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}