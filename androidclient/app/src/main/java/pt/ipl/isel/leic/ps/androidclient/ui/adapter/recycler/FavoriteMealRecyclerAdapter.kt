package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.FavoriteMealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.FavoriteMealRecyclerViewModel

class FavoriteMealRecyclerAdapter(
    model: FavoriteMealRecyclerViewModel,
    ctx: Context,
    private val isCalculatorMode: Boolean
) : ARecyclerAdapter<MealItem, FavoriteMealRecyclerViewModel, FavoriteMealRecyclerViewHolder>(
    model,
    ctx
) {
    override fun onBindViewHolder(holder: FavoriteMealRecyclerViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item: MealItem = viewModel.items[position]
        holder.bindTo(item)
        holder.isCalculatorMode = isCalculatorMode
        holder.onDelete = { viewModel.deleteItem(item) }
    }

    override fun getItemViewId(): Int = R.layout.meal_card

    override fun newViewHolder(layout: ViewGroup): FavoriteMealRecyclerViewHolder =
        FavoriteMealRecyclerViewHolder(layout, ctx)
}
