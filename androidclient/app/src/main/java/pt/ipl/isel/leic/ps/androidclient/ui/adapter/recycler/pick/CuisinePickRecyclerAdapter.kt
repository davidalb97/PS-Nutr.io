package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.FilterRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel

class CuisinePickRecyclerAdapter(
    model: CuisinePickViewModel,
    ctx: Context
) : BasePickRecyclerAdapter<Cuisine, CuisinePickViewModel>(
    viewModel = model,
    ctx = ctx
) {
    override fun newViewHolder(layout: View): FilterRecyclerViewHolder<Cuisine> {
        return object : FilterRecyclerViewHolder<Cuisine>(
            navDestination = Navigation.IGNORE,
            view = layout,
            ctx = ctx
        ) {
            override fun onDelete() = viewModel.unPick(item)
            override fun getName(): String = item.name
        }
    }
}