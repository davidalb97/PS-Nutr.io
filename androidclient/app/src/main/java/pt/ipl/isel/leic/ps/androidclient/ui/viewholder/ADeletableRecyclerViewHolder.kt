package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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

    @SuppressLint("ResourceType")
    override fun onLongClick(v: View?): Boolean {
        val builder = AlertDialog.Builder(ctx)

        val currentAdapter = this.bindingAdapter

        builder.setTitle(view.context.getString(R.string.DialogAlert_deleteWarning))

        builder.setMessage(view.context.getString(R.string.AlertDialog_deleteQuestion))

        builder.setPositiveButton(view.context.getString(R.string.DialogAlert_Yes)) { _, _ ->

            onDelete(this.item)
            Toast.makeText(
                ctx,
                ctx.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
            ).show()
            currentAdapter?.notifyItemRemoved(layoutPosition)
        }

        builder.setNegativeButton(view.context.getString(R.string.Dialog_no)) { _, _ -> }

        val dialog: AlertDialog = builder.create()

        dialog.show()
        return true
    }
}