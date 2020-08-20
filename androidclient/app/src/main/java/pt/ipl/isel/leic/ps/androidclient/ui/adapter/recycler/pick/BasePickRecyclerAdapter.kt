package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick

import android.content.Context
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.BaseRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.BaseRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.ItemPickerViewModel

abstract class BasePickRecyclerAdapter<T : Parcelable, VM : ItemPickerViewModel<T>>(
    viewModel: VM,
    ctx: Context
) : BaseRecyclerAdapter<T, VM, BaseRecyclerViewHolder<T>>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T>, position: Int) {
        holder.bindTo(viewModel.pickedItems[position])
    }

    override fun getItemCount(): Int = viewModel.pickedItems.size

    override fun getItemViewId(): Int = R.layout.filter_item
}
