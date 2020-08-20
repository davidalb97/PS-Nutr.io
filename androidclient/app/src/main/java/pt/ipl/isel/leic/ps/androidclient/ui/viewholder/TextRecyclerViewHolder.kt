package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

//TODO delete TextRecyclerViewHolder if not in use
class TextRecyclerViewHolder(
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseRecyclerViewHolder<String>(
    navDestination = navDestination,
    view = view,
    ctx = ctx
) {
    private val textView: TextView = view as TextView

    override fun bindTo(item: String) {
        super.bindTo(item)
        textView.text = item
    }

    override fun onSendToDestination(bundle: Bundle) {

    }
}