package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.RestaurantItemRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.RestaurantListViewModel

class RestaurantRecyclerAdapter(
    viewModel: RestaurantListViewModel,
    ctx: Context
) : BaseRecyclerAdapter<RestaurantItem, RestaurantListViewModel, RestaurantItemRecyclerViewHolder>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun getItemViewId(): Int = R.layout.restaurant_card

    override fun newViewHolder(layout: View): RestaurantItemRecyclerViewHolder {
        return object : RestaurantItemRecyclerViewHolder(
            navDestination = viewModel.navDestination,
            actions = viewModel.actions,
            view = layout,
            ctx = ctx
        ) {
            override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
                viewModel.putFavorite(this.item, onSuccess, onError)
            }

            override fun onReport(
                reportStr: String,
                onSuccess: () -> Unit,
                onError: (Throwable) -> Unit
            ) {
                viewModel.report(this.item, reportStr, onSuccess, onError)
            }
        }
    }
}