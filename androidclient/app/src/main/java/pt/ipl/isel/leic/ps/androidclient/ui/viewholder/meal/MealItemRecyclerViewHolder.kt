package pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal

import android.content.Context
import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putMealItem

abstract class MealItemRecyclerViewHolder(
    onEditNavigation: Navigation? = null,
    actions: List<ItemAction>,
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseMealRecyclerViewHolder<MealItem>(
    onEditNavigation = onEditNavigation,
    navDestination = navDestination,
    actions = actions,
    view = view,
    ctx = ctx
) {
    override fun onSendToDestination(bundle: Bundle) {
        super.onSendToDestination(bundle)
        bundle.putMealItem(this.item)
    }
}