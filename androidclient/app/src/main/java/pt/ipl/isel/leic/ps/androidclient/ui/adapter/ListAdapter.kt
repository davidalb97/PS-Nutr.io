package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AViewModel

// GameListHolder
class ListHolder<T>(view: ViewGroup) : RecyclerView.ViewHolder(view) {

    fun bindTo(
        item: T?,
        viewModel: AViewModel<T>,
        pos: Int
    ) {


    }

}

class ListAdapter<T>(private val viewModel: AViewModel<T>) :
    RecyclerView.Adapter<ListHolder<T>>() {

    override fun onBindViewHolder(holder: ListHolder<T>, position: Int) {
        holder.bindTo(
            viewModel.liveData?.value?.get(position),
            viewModel,
            position
        )
    }

    override fun getItemCount(): Int = viewModel.liveData?.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder<T> =
        ListHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_card_view, parent, false
                ) as ViewGroup
        )

}
