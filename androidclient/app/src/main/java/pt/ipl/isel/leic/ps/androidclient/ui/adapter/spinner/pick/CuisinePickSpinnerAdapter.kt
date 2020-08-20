package pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.BaseRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.CuisineRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel

class CuisinePickSpinnerAdapter(
    viewModel: CuisinePickViewModel,
    ctx: Context
) : BasePickSpinnerAdapter<Cuisine, CuisinePickViewModel>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun newDropDownItemViewHolder(layout: View): BaseRecyclerViewHolder<Cuisine> {
        return CuisineRecyclerViewHolder(
            navDestination = Navigation.IGNORE,
            view = layout,
            ctx = ctx
        ).also { viewHolder ->
            setOnClickHandler(viewHolder)
        }
    }
}