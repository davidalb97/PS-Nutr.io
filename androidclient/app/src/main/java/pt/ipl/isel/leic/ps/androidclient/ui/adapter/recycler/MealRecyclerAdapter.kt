package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.ARecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.MealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

class MealRecyclerAdapter(
    model: MealRecyclerViewModel,
    ctx: Context
) : ARecyclerAdapter<Meal, MealRecyclerViewModel, MealRecyclerViewHolder>(model, ctx) {

    override fun getItemViewId(): Int = R.layout.meal_card

    override fun newViewHolder(layout: ViewGroup): MealRecyclerViewHolder =
        MealRecyclerViewHolder(layout, ctx)
}
