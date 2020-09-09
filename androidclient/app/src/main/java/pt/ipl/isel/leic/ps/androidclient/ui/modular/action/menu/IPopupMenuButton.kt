package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext

interface IPopupMenuButton : IMenu, IContext {

    val menuButtonId: Int
    var menuButton: ImageButton

    fun setupPopupMenuButton(view: View) {
        menuButton = view.findViewById(menuButtonId)

        if (menus.isEmpty()) {
            return
        }
        menuButton.setOnClickListener { viewAnchor ->
            val popupMenu = PopupMenu(fetchCtx(), viewAnchor)
            val menu = popupMenu.menu
            populateMenu(menu)
            popupMenu.show()
        }
        menuButton.visibility = View.VISIBLE
    }
}