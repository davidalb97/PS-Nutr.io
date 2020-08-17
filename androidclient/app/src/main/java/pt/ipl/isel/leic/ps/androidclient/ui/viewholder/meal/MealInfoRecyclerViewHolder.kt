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

abstract class MealInfoRecyclerViewHolder(
    actions: List<ItemAction>,
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseMealRecyclerViewHolder<MealInfo>(
    navDestination = navDestination,
    actions = actions,
    view = view,
    ctx = ctx
), IMealItemDetail<MealInfo> {

    override val customMealQuantity: TextView = view.findViewById(R.id.custom_meal_quantity)
    override val customMealCarbs: TextView = view.findViewById(R.id.custom_meal_carbs_amount)

    override fun bindTo(item: MealInfo) {
        super.bindTo(item)
        setupCustomMeal(ctx, item)
    }

    override fun onSendToDestination(bundle: Bundle) {
        super.onSendToDestination(bundle)
        bundle.putMealInfo(this.item)
    }
}