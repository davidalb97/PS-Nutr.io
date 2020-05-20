package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R

abstract class ADeletableRecyclerViewHolder<T : Any>(
    view: ViewGroup
) : ARecyclerViewHolder<T>(view),
    View.OnLongClickListener {

    override fun onLongClick(v: View?): Boolean {
        val builder = AlertDialog.Builder(view.context)

        builder.setTitle(view.context.getString(R.string.DialogAlert_deleteWarning))

        builder.setMessage(view.context.getString(R.string.AlertDialog_deleteQuestion))

        builder.setPositiveButton(view.context.getString(R.string.DialogAlert_Yes)) { _, _ ->

             //deleteFunction(element)
             Toast.makeText(
                 view.context,
                 view.context.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
             ).show()
             /*elementList.removeAt(position)
             notifyItemRemoved(position)
             notifyDataSetChanged()*/
        }

        builder.setNegativeButton(view.context.getString(R.string.Dialog_no)) { _, _ -> }

        val dialog: AlertDialog = builder.create()

        dialog.show()

        return true
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}