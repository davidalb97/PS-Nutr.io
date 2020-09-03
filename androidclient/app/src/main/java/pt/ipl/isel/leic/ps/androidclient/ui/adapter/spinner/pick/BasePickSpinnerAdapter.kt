package pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.BaseSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.BaseRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.BaseItemPickerViewModel

abstract class BasePickSpinnerAdapter<M : Parcelable, VM : BaseItemPickerViewModel<M>>(
    viewModel: VM,
    ctx: Context
) : BaseSpinnerAdapter<M, VM>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun newSelectedItemViewHolder(layout: View): BaseRecyclerViewHolder<M> {
        return object : BaseRecyclerViewHolder<M>(
            navDestination = Navigation.IGNORE,
            view = layout,
            ctx = ctx
        ) {
            init {
                onClickListener = null
            }

            override fun onSendToDestination(bundle: Bundle) {
            }
        }
    }

    protected fun setOnClickHandler(viewHolder: BaseRecyclerViewHolder<M>) {
        viewHolder.onClickListener = viewHolder.onClickListener?.appendListener { model, idx ->
            viewModel.pick(model)
        }
    }
}