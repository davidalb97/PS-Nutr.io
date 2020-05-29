package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R

abstract class ADeletableRecyclerViewHolder<T : Parcelable>(
    view: ViewGroup,
    ctx: Context
) : ARecyclerViewHolder<T>(view, ctx) {

    lateinit var onDelete: (T) -> Unit
    lateinit var deleteButton: ImageButton

    override fun onClick(v: View?) {
        turnButtonsInvisible()
    }

    override fun onLongClick(v: View?): Boolean {
        turnButtonsVisible()
        setupDeleteButton()
        setupAddToCalculatorButton()
        return true
    }

    private fun setupDeleteButton() {
        deleteButton.setOnClickListener {
            onDelete(this.item)
            Toast.makeText(
                ctx,
                ctx.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
            ).show()
            turnButtonsInvisible()
            this.bindingAdapter?.notifyItemRemoved(layoutPosition)
        }
    }

    override fun turnButtonsVisible() {
        super.turnButtonsVisible()
        deleteButton.visibility = View.VISIBLE
    }

    override fun turnButtonsInvisible() {
        super.turnButtonsInvisible()
        deleteButton.visibility = View.INVISIBLE
    }
}