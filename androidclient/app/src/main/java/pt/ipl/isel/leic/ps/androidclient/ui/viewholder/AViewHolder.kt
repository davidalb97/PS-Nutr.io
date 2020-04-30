package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AViewHolder<T : Any>(
    val view: ViewGroup
) : RecyclerView.ViewHolder(view) {

    lateinit var item: T

    open fun bindTo(item: T) {
        this.item = item
    }
}