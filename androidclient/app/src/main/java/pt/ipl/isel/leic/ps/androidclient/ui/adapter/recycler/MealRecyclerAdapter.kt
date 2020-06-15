package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.MealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

class MealRecyclerAdapter(
    model: MealRecyclerViewModel,
    ctx: Context,
    val isCalculatorMode: Boolean
) : ARecyclerAdapter<MealItem, MealRecyclerViewModel, MealRecyclerViewHolder>(model, ctx) {

    override fun onBindViewHolder(holder: MealRecyclerViewHolder, position: Int) {
        val item: MealItem = viewModel.items[position]
        holder.bindTo(item)
        holder.isCalculatorMode = isCalculatorMode
    }

    override fun getItemViewId(): Int = R.layout.meal_card

    override fun newViewHolder(layout: ViewGroup): MealRecyclerViewHolder =
        MealRecyclerViewHolder(layout, ctx)
}
