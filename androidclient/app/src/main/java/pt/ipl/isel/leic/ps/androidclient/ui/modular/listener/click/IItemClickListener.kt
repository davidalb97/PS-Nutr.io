package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click

fun interface IItemClickListener<T> {

    fun onClick(item: T, onChangeCallback: () -> Unit)

    fun appendListener(newListener: IItemClickListener<T>) =
        IItemClickListener<T> { item, onChangeCallback ->
            newListener.onClick(item, onChangeCallback)
            this.onClick(item, onChangeCallback)
        }
}