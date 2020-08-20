package pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.BaseRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel

abstract class BaseSpinnerAdapter<M : Parcelable, VM : BaseListViewModel<M>>(
    val viewModel: VM,
    val ctx: Context
) : ArrayAdapter<M>(ctx, android.R.layout.simple_spinner_item) { //BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(
            idx = position,
            convertView = convertView,
            parent = parent,
            layout = getSelectedItemLayoutId(),
            newViewHolder = ::newSelectedItemViewHolder
        )
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(
            idx = position,
            convertView = convertView,
            parent = parent,
            layout = getDropDownItemLayoutId(),
            newViewHolder = ::newDropDownItemViewHolder
        )
    }

    open fun createView(
        idx: Int,
        convertView: View?,
        parent: ViewGroup,
        @LayoutRes layout: Int,
        newViewHolder: (View) -> BaseRecyclerViewHolder<M>
    ): View {
        val newView: View
        var viewHolder: BaseRecyclerViewHolder<M>? = null
        if (convertView == null) {
            newView = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
            viewHolder = newViewHolder(newView)
        } else {
            newView = convertView
            viewHolder = convertView.tag as BaseRecyclerViewHolder<M>
        }
        val item = getItem(idx)
        viewHolder.bindTo(item)
        newView.tag = viewHolder
        return newView
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItem(position: Int): M = viewModel.items[position]

    override fun getCount(): Int = viewModel.items.size

    @LayoutRes
    open fun getSelectedItemLayoutId(): Int = android.R.layout.simple_spinner_item

    @LayoutRes
    open fun getDropDownItemLayoutId(): Int = android.R.layout.simple_spinner_dropdown_item

    abstract fun newSelectedItemViewHolder(layout: View): BaseRecyclerViewHolder<M>

    abstract fun newDropDownItemViewHolder(layout: View): BaseRecyclerViewHolder<M>
}