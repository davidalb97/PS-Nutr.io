package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext

interface IPopupMenuButton : IMenu, IContext {

    val menuButton: ImageButton

    fun setupPopupMenuButton() {
        if (menus.isEmpty()) {
            return
        }
        menuButton.setOnClickListener {
            newMenu(it)
        }
        menuButton.visibility = View.VISIBLE
    }

    fun newMenu(viewAnchor: View) {
        val popupMenu = PopupMenu(fetchCtx(), viewAnchor)
        val menu = popupMenu.menu
        menus.forEach { menuItemFactory ->
            menuItemFactory.newMenuItem(menu)
        }
        popupMenu.show()
    }
}