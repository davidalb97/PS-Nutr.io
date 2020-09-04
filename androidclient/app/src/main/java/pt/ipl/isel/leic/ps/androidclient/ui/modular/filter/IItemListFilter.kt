package pt.ipl.isel.leic.ps.androidclient.ui.modular.filter

fun interface IItemListFilter<T> {

    fun filter(newItem: T): Boolean
}