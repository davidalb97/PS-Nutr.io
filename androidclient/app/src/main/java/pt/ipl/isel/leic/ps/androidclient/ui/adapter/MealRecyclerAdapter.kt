package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.MealViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel


// TODO: Change item view Id when meal_item exists
class MealRecyclerAdapter(
    model: MealRecyclerViewModel,
    ctx: Context
) : ARecyclerAdapter<Meal, MealRecyclerViewModel, MealViewHolder>(model, ctx) {

    override fun getItemViewId(): Int = R.layout.restaurant_card

    override fun newViewHolder(layout: ViewGroup): MealViewHolder =
        MealViewHolder(layout, ctx)
}
