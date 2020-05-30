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
) : ARecyclerViewHolder<CustomMealDto>(view, ctx),
    IDeletable<CustomMealDto>,
    ICalculatable<CustomMealDto> {

    var addMode: Boolean = false
    override lateinit var onDelete: (CustomMealDto) -> Unit

    private val customMealName: TextView =
        view.findViewById(R.id.custom_meal_name)
    private val customMealQuantity: TextView =
        view.findViewById(R.id.custom_meal_quantity)
    private val customMealGlucose: TextView =
        view.findViewById(R.id.custom_meal_glucose_amount)
    private val customMealCarbs: TextView =
        view.findViewById(R.id.custom_meal_carbs_amount)
    override var deleteButton: ImageButton =
        view.findViewById(R.id.delete_item_button)
    override var calculatorButton: ImageButton =
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
    }

    private fun setupListeners() {
        this.view.setOnClickListener(this)
        this.view.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        turnInvisible()
        if (addMode) {
            sendToCalculator()
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (!addMode) {
            turnVisible()
            deleteButton.setOnClickListener {
                onDelete(this.item)
                Toast.makeText(
                    ctx,
                    ctx.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
                ).show()
                turnInvisible()
                this.bindingAdapter?.notifyItemRemoved(layoutPosition)
            }

            calculatorButton.setOnClickListener {
                sendToCalculator()
            }
            return true
        }
        return true
    }

    override fun turnVisible() {
        deleteButton.visibility = View.VISIBLE
        calculatorButton.visibility = View.VISIBLE
    }

    override fun turnInvisible() {
        deleteButton.visibility = View.INVISIBLE
        calculatorButton.visibility = View.INVISIBLE
    }

    private fun sendToCalculator() {
        val bundle = Bundle()
        bundle.putParcelable("bundledMeal", this.item)
        view.findNavController().navigate(R.id.nav_calculator, bundle)
    }
}