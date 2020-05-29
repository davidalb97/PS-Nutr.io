package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R

/**
 * A generic View Holder for Recycler Lists
 */
abstract class ARecyclerViewHolder<T : Parcelable>(
    val view: ViewGroup,
    val ctx: Context
) : RecyclerView.ViewHolder(view) {

    lateinit var item: T
    //lateinit var addToCalculatorButton: ImageButton

    open fun bindTo(item: T) {
        this.item = item
    }

    /*override fun onClick(v: View?) {
        turnButtonsInvisible()
    }

    override fun onLongClick(v: View?): Boolean {
        turnButtonsVisible()
        setupAddToCalculatorButton()
        return true
    }

    fun setupAddToCalculatorButton() {
        addToCalculatorButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("bundledMeal", this.item)
            turnButtonsInvisible()
            view.findNavController().navigate(R.id.nav_calculator, bundle)
        }
    }

    open fun turnButtonsVisible() {
        addToCalculatorButton.visibility = View.VISIBLE
    }

    open fun turnButtonsInvisible() {
        addToCalculatorButton.visibility = View.INVISIBLE
    }*/
}