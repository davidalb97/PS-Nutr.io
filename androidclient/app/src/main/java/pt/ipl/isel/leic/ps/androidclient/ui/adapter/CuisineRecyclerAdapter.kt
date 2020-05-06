package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.CuisineRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CuisineRecyclerViewModel

class CuisineRecyclerAdapter(
    model: CuisineRecyclerViewModel,
    ctx: Context
) : ARecyclerAdapter<Cuisine, CuisineRecyclerViewModel, CuisineRecyclerViewHolder>(model, ctx) {

    override fun getItemViewId(): Int = R.layout.cuisine_card

    override fun newViewHolder(layout: ViewGroup): CuisineRecyclerViewHolder =
        CuisineRecyclerViewHolder(layout, ctx)
}