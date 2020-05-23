package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.ARecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

/**
 *  A generic Adapter for Recycler Views.
 */
abstract class ARecyclerAdapter<T : Any, ViewModelType : ARecyclerViewModel<T>, ViewHolderType : ARecyclerViewHolder<T>>(
    val viewModel: ViewModelType,
    val ctx: Context
) : RecyclerView.Adapter<ViewHolderType>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderType {
        val view = LayoutInflater.from(parent.context)
            .inflate(getItemViewId(), parent, false) as ViewGroup
        return newViewHolder(view)
    }

    override fun getItemCount(): Int = viewModel.items.size


    @LayoutRes
    abstract fun getItemViewId(): Int

    abstract fun newViewHolder(layout: ViewGroup): ViewHolderType
}
