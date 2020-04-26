package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R

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
            viewModel.liveInfo.value?.list?.get(position),
            viewModel,
            position
        )
    }

    override fun getItemCount(): Int = viewModel.liveInfo.value?.list?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder<T> =
        // TODO - create a xml cardview item for the lists
        ListHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_home, parent, false) as ViewGroup
        )

}
