package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

class CuisineRecyclerViewHolder(view: ViewGroup, ctx: Context) :
    ARecyclerViewHolder<Cuisine>(view, ctx) {

    val cuisineName = view.findViewById<TextView>(R.id.cuisine_name)

    override fun bindTo(item: Cuisine) {
        super.bindTo(item)
        cuisineName.text = item.name
    }

}