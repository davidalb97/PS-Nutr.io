package pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal

import android.content.Context
import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putMealIngredient

abstract class IngredientRecyclerViewHolder(
    actions: List<ItemAction>,
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseMealRecyclerViewHolder<MealIngredient>(
    navDestination = navDestination,
    actions = actions,
    view = view,
    ctx = ctx
) {

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putMealIngredient(this.item)
    }
}