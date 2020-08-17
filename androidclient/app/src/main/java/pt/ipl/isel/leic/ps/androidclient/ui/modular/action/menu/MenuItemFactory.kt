package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

import android.view.Menu
import android.view.MenuItem

abstract class MenuItemFactory {

    abstract fun newMenuItem(menu: Menu): MenuItem
}