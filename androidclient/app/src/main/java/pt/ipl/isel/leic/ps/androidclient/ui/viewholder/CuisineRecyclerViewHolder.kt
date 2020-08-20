package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

class CuisineRecyclerViewHolder(
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseRecyclerViewHolder<Cuisine>(
    navDestination = navDestination,
    view = view,
    ctx = ctx
) {
    private val textView: TextView = view as TextView

    override fun bindTo(item: Cuisine) {
        super.bindTo(item)
        textView.text = item.name
    }

    override fun onSendToDestination(bundle: Bundle) {

    }
}