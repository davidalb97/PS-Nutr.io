package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.RestaurantRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantRecyclerViewModel

class RestaurantRecyclerAdapter(
    model: RestaurantRecyclerViewModel,
    ctx: Context
) : ARecyclerAdapter<RestaurantItem, RestaurantRecyclerViewModel, RestaurantRecyclerViewHolder>(model, ctx) {

    override fun getItemViewId(): Int = R.layout.restaurant_card

    override fun newViewHolder(layout: ViewGroup): RestaurantRecyclerViewHolder =
        RestaurantRecyclerViewHolder(layout, ctx)
}