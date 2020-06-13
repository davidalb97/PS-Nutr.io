package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.MealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ApiMealRecyclerViewModel

class MealRecyclerAdapter(
    model: ApiMealRecyclerViewModel,
    ctx: Context
) : ARecyclerAdapter<MealItem, ApiMealRecyclerViewModel, MealRecyclerViewHolder>(model, ctx) {

    override fun getItemViewId(): Int = R.layout.meal_card

    override fun newViewHolder(layout: ViewGroup): MealRecyclerViewHolder =
        MealRecyclerViewHolder(layout, ctx)
}
