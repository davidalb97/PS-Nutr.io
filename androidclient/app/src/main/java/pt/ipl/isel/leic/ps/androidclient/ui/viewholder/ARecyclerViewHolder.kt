package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic View Holder for Recycler Lists
 */
abstract class ARecyclerViewHolder<T : Any>(
    val view: ViewGroup
) : RecyclerView.ViewHolder(view),
    View.OnClickListener {

    lateinit var item: T

    open fun bindTo(item: T) {
        this.item = item
    }
}