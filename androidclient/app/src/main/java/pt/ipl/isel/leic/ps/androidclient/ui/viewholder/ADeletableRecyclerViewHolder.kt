package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R

abstract class ADeletableRecyclerViewHolder<T : Any>(
    view: ViewGroup,
    val ctx: Context
) : ARecyclerViewHolder<T>(view),
    View.OnLongClickListener {

    lateinit var onDelete: (T) -> Unit
    lateinit var list: MutableList<T>

    override fun onLongClick(v: View?): Boolean {
        val builder = AlertDialog.Builder(view.context)
        val currentAdapter = this.bindingAdapter

        builder.setTitle(view.context.getString(R.string.DialogAlert_deleteWarning))

        builder.setMessage(view.context.getString(R.string.AlertDialog_deleteQuestion))

        builder.setPositiveButton(view.context.getString(R.string.DialogAlert_Yes)) { _, _ ->

            onDelete(this.item)
            Toast.makeText(
                view.context,
                view.context.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
            ).show()
            this.list.removeAt(layoutPosition)
            currentAdapter?.notifyItemRemoved(layoutPosition)
            currentAdapter?.notifyDataSetChanged()
        }

        builder.setNegativeButton(view.context.getString(R.string.Dialog_no)) { _, _ -> }

        val dialog: AlertDialog = builder.create()

        dialog.show()

        return true
    }
}