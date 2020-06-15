package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.BUNDLED_MEAL_INFO_TAG
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_MEAL_DB_ID
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_MEAL_RESTAURANT_SUBMISSION_ID
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_MEAL_SOURCE
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_MEAL_SUBMISSION_ID

class CustomMealRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ARecyclerViewHolder<MealItem>(view, ctx),
    IDeletable<MealItem>,
    ICalculatable<MealItem> {

    override lateinit var onDelete: (MealItem) -> AsyncWorker<Unit, Unit>

    private val customMealName: TextView = view.findViewById(R.id.custom_meal_name)
    private val customMealQuantity: TextView = view.findViewById(R.id.custom_meal_quantity)
    private val customMealCarbs: TextView = view.findViewById(R.id.custom_meal_carbs_amount)
    override var deleteButton: ImageButton = view.findViewById(R.id.delete_item_button)
    override var calculatorButton: ImageButton = view.findViewById(R.id.add_meal_to_calc)

    override fun bindTo(item: MealItem) {
        super.bindTo(item)
        setupViewHolderElements()
        setupListeners()
    }

    private fun setupViewHolderElements() {
        val resources = ctx.resources
        customMealName.text = item.name
        customMealQuantity.text = String.format(resources.getString(
            R.string.meal_quantity_card),
            item.amount,
            item.unit
        )
        customMealCarbs.text = String.format(resources.getString(
            R.string.carbohydrates_amount_card),
            item.carbs
        )
    }

    private fun setupListeners() {
        this.view.setOnClickListener(this)
        this.view.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        setButtonsVisibility(false)
        if (isCalculatorMode) {
            sendToCalculator()
        } else {
            sendToMealDetail()
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (!isCalculatorMode) {
            setButtonsVisibility(true)
            deleteButton.setOnClickListener {
                onDelete(this.item)
                    .setOnPostExecute {
                        this.bindingAdapter?.notifyItemRemoved(layoutPosition)
                        Toast.makeText(
                            ctx,
                            ctx.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
                        ).show()
                        setButtonsVisibility(false)
                    }.execute()
            }

            calculatorButton.setOnClickListener {
                sendToCalculator()
            }
            return true
        }
        return true
    }

    override fun setButtonsVisibility(isVisible: Boolean) {
        val visibility =
            if (isVisible) View.VISIBLE else View.INVISIBLE
        deleteButton.visibility = visibility
        calculatorButton.visibility = visibility
    }

    private fun sendToCalculator() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_MEAL_SOURCE, this.item.source.ordinal)
        bundle.putInt(BUNDLE_MEAL_SUBMISSION_ID, this.item.submissionId)
        bundle.putString(BUNDLE_MEAL_RESTAURANT_SUBMISSION_ID, this.item.restaurantSubmissionId)
        bundle.putLong(BUNDLE_MEAL_DB_ID, this.item.dbId)
        view.findNavController().navigate(R.id.nav_calculator, bundle)
    }

    private fun sendToMealDetail() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_MEAL_SOURCE, this.item.source.ordinal)
        bundle.putInt(BUNDLE_MEAL_SUBMISSION_ID, this.item.submissionId)
        bundle.putString(BUNDLE_MEAL_RESTAURANT_SUBMISSION_ID, this.item.restaurantSubmissionId)
        bundle.putLong(BUNDLE_MEAL_DB_ID, this.item.dbId)
        view.findNavController().navigate(R.id.nav_meal_detail, bundle)
    }
}