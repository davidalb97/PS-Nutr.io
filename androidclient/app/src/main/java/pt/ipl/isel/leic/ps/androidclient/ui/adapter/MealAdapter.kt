package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.content.Context
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_card_view.view.*
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.MealViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealViewModel


// TODO: Change Any types, change item view Id when meal_item exists
class MealAdapter(
    model: MealViewModel,
    ctx: Context
) : AAdapter<Any, MealViewModel, MealViewHolder>(model, ctx) {

    override fun getItemViewId(): Int = R.layout.restaurant_item

    override fun newViewHolder(layout: ViewGroup): MealViewHolder =
        MealViewHolder(layout, ctx)
}
