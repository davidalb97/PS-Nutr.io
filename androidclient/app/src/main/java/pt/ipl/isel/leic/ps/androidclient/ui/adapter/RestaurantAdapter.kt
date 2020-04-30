package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.RestaurantViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantViewModel

class RestaurantAdapter(
    model: RestaurantViewModel,
    ctx: Context
): AAdapter<Restaurant, RestaurantViewModel, RestaurantViewHolder>(model, ctx) {



    override fun getItemViewId(): Int {
        TODO("Not yet implemented")
    }

    override fun newViewHolder(layout: ViewGroup): RestaurantViewHolder {
        TODO("Not yet implemented")
    }

}