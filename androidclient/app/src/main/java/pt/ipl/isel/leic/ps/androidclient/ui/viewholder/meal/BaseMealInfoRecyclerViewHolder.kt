package pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder.IMealItemDetail
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putMealInfo

abstract class BaseMealInfoRecyclerViewHolder<T : MealInfo>(
    actions: List<ItemAction>,
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseMealRecyclerViewHolder<T>(
    actions = actions,
    navDestination = navDestination,
    view = view,
    ctx = ctx
), IMealItemDetail<T> {

    override val customMealQuantityId: Int = R.id.custom_meal_quantity
    override lateinit var customMealQuantity: TextView
    override val customMealCarbsId: Int = R.id.custom_meal_carbs_amount
    override lateinit var customMealCarbs: TextView

    override fun bindTo(item: T) {
        super.bindTo(item)

        super.setupCustomMeal(view, ctx, item)
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putMealInfo(item)
    }
}