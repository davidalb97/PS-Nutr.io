package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbCustomMealDto
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.CustomMealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CustomMealRecyclerViewModel

class CustomMealRecyclerAdapter(
    model: CustomMealRecyclerViewModel,
    ctx: Context,
    private val isCalculatorMode: Boolean
) : ARecyclerAdapter<DbCustomMealDto, CustomMealRecyclerViewModel, CustomMealRecyclerViewHolder>(
    model,
    ctx
) {

    override fun onBindViewHolder(holder: CustomMealRecyclerViewHolder, position: Int) {
        val item: DbCustomMealDto = viewModel.items[position]
        holder.bindTo(item)
        holder.isCalculatorMode = isCalculatorMode
        holder.onDelete = { viewModel.deleteItem(item) }
    }

    override fun getItemViewId(): Int = R.layout.custom_meal_card

    override fun newViewHolder(layout: ViewGroup): CustomMealRecyclerViewHolder =
        CustomMealRecyclerViewHolder(layout, ctx)
}