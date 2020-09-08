package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check

import android.view.View
import android.widget.CheckBox
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction

interface ICheckBox<T> : IAction {

    var checkBox: CheckBox
    val checkBoxId: Int

    fun onCheck(isChecked: Boolean)

    fun isRestored(): Boolean

    fun setupCheckBox(view: View, item: T) {
        if(!actions.contains(ItemAction.CHECK)) {
            return
        }
        checkBox = view.findViewById(checkBoxId)
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = isRestored()
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheck(isChecked)
        }
        checkBox.visibility = View.VISIBLE
    }
}