package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.FilterRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.MealItemPickViewModel

class MealItemPickRecyclerAdapter(
    model: MealItemPickViewModel,
    ctx: Context
) : BasePickRecyclerAdapter<MealItem, MealItemPickViewModel>(
    viewModel = model,
    ctx = ctx
) {
    override fun newViewHolder(layout: View): FilterRecyclerViewHolder<MealItem> {
        return object : FilterRecyclerViewHolder<MealItem>(
            navDestination = Navigation.IGNORE,
            view = layout,
            ctx = ctx
        ) {
            override fun onDelete() = viewModel.unPick(item)
            override fun getName(): String = item.name
        }
    }
}