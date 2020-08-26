package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click

fun interface IItemClickListener<T> {

    fun onClick(item: T, idx: Int)

    fun appendListener(newListener: IItemClickListener<T>) = IItemClickListener<T> { item, idx ->
        newListener.onClick(item, idx)
        this.onClick(item, idx)
    }
}