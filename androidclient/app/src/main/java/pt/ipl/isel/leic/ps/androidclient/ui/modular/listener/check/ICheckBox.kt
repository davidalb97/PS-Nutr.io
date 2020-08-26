package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check

import android.view.View
import android.widget.CheckBox
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction

interface ICheckBox<T> : IAction {

    var checkBox: CheckBox
    val checkBoxId: Int

    fun onCheck(isChecked: Boolean)

    fun isAlreadyChecked(): Boolean

    fun setupCheckBox(view: View, item: T) {
        if(!actions.contains(ItemAction.CHECK)) {
            return
        }
        checkBox = view.findViewById(checkBoxId)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheck(isChecked)
        }
        if(isAlreadyChecked()) {
            checkBox.isChecked = true
        }
        checkBox.visibility = View.VISIBLE
    }
}