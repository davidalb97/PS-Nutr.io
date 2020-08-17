package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

import android.view.Menu
import android.view.MenuItem
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction

interface IEditMenuItem : IMenu, IAction {

    fun onEdit(onSuccess: () -> Unit)

    fun setupEditMenuItem() {
        if (!actions.contains(ItemAction.EDIT)) {
            return
        }
        menus.add(object : MenuItemFactory() {
            override fun newMenuItem(menu: Menu): MenuItem {
                return menu.add(R.string.edit_menu_item_title).also { menuItem ->
                    menuItem.setOnMenuItemClickListener {
                        onEdit {
                            menu.close()
                        }
                        true
                    }
                }
            }
        })
    }
}