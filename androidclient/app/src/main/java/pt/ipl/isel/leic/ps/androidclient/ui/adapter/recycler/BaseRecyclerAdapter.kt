package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.BaseRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel

/**
 *  A generic Adapter for Recycler Views.
 */
abstract class BaseRecyclerAdapter
<M : Parcelable, VM : BaseListViewModel<M>, VH : BaseRecyclerViewHolder<M>>(
    val viewModel: VM,
    val ctx: Context
) : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(getItemViewId(), parent, false) as ViewGroup

        return newViewHolder(view)
    }

    override fun getItemCount(): Int = viewModel.items.size

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bindTo(viewModel.items[position])

    @LayoutRes
    abstract fun getItemViewId(): Int

    abstract fun newViewHolder(layout: View): VH
}
