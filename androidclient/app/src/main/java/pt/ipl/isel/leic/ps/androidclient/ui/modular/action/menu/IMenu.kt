package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

interface IMenu {
    val menus: MutableMap<String, MenuItemFactory>
}