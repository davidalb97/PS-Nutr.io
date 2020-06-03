package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic View Holder for Recycler Lists
 */
abstract class ARecyclerViewHolder<T : Parcelable>(
    val view: ViewGroup,
    val ctx: Context
) : RecyclerView.ViewHolder(view) {

    lateinit var item: T

    open fun bindTo(item: T) {
        this.item = item
    }
    
}