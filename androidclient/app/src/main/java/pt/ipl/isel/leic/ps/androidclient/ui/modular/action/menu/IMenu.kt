package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

import android.view.Menu

interface IMenu {
    val menus: MutableMap<String, MenuItemFactory>

    fun populateMenu(menu: Menu?) {
        if(menu == null) {
            return
        }
        menus.values.forEach { menuItemFactory ->
            menuItemFactory.newMenuItem(menu)
        }
    }
}