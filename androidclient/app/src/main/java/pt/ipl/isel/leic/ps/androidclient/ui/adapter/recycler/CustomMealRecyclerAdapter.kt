package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.CustomMealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.InsulinProfileRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CustomMealRecyclerViewModel

class CustomMealRecyclerAdapter(
    model: CustomMealRecyclerViewModel,
    ctx: Context
) : ARecyclerAdapter<CustomMealDto, CustomMealRecyclerViewModel, CustomMealRecyclerViewHolder>(
    model,
    ctx
) {

    override fun onBindViewHolder(holder: CustomMealRecyclerViewHolder, position: Int) {
        val item: CustomMealDto = viewModel.items[position]
        holder.bindTo(item)
        holder.onDelete = { viewModel.deleteItem(item) }
    }

    override fun getItemViewId(): Int = R.layout.custom_meal_card

    override fun newViewHolder(layout: ViewGroup): CustomMealRecyclerViewHolder =
        CustomMealRecyclerViewHolder(layout, ctx)
}