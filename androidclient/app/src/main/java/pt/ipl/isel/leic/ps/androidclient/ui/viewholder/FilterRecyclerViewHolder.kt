package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

abstract class FilterRecyclerViewHolder<T : Any>(
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseRecyclerViewHolder<T>(
    navDestination = navDestination,
    view = view,
    ctx = ctx
) {
    private val nameButton: Button = view.findViewById(R.id.filter_item_name)
    private val crossButton: Button = view.findViewById(R.id.filter_item_del_btn)

    override fun bindTo(item: T) {
        super.bindTo(item)
        nameButton.text = getName()
        crossButton.setOnClickListener {
            onDelete()
        }
    }

    abstract fun onDelete()

    abstract fun getName(): String

    override fun onSendToDestination(bundle: Bundle) {

    }
}