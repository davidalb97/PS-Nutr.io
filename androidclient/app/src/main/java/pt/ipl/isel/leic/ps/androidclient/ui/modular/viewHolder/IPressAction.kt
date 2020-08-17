package pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder

import android.view.View
import android.widget.RelativeLayout
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IAction

interface IPressAction<T : Any> : IAction, View.OnLongClickListener, IClickListener<T> {

    val pressActionViewId: Int
    var pressActionView: RelativeLayout

    fun setupPressAction(view: View) {

        pressActionView = view.findViewById(pressActionViewId)

        view.setOnLongClickListener {
            setButtonsVisibility(true)
            true
        }
        val oldClickListener = onClickListener
        onClickListener = {
            setButtonsVisibility(false)
            oldClickListener?.invoke(it)
        }
    }

    fun setButtonsVisibility(isVisible: Boolean) {
        if (actions.isNotEmpty()) {
            val visibility = if (isVisible) View.VISIBLE else View.GONE
            pressActionView.visibility = visibility
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (actions.isNotEmpty()) {
            setButtonsVisibility(true)
        }
        return true
    }
}