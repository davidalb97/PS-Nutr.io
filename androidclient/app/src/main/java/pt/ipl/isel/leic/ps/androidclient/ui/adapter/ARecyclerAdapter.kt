package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.AViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AViewModel

abstract class AAdapter<T : Any, ViewM : AViewModel<T>, ViewH : AViewHolder<T>>(
    val viewModel: ViewM,
    val ctx: Context
) : RecyclerView.Adapter<ViewH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
        val view = LayoutInflater.from(parent.context)
            .inflate(getItemViewId(), parent, false) as ViewGroup
        return newViewHolder(view)
    }

    override fun getItemCount(): Int = viewModel.items.size

    override fun onBindViewHolder(holder: ViewH, position: Int) =
        holder.bindTo(viewModel.items[position])

    @LayoutRes
    abstract fun getItemViewId(): Int

    abstract fun newViewHolder(layout: ViewGroup): ViewH
}
