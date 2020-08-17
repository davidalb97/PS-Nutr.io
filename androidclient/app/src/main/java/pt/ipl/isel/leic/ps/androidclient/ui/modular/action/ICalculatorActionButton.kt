package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

import android.view.View
import android.widget.ImageButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

interface ICalculatorActionButton : ISend, IAction {

    val calculatorButtonId: Int
    var calculatorButton: ImageButton

    fun setupCalculateAction(view: View) {
        calculatorButton = view.findViewById(calculatorButtonId)

        if (!actions.contains(ItemAction.CALCULATE)) {
            return
        }
        calculatorButton.setOnClickListener {
            sendToDestination(view, Navigation.SEND_TO_CALCULATOR)
        }
        calculatorButton.visibility = View.VISIBLE
    }
}