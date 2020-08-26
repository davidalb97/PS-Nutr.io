package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check

fun interface ICheckListener<T> {

    fun onCheckChange(item: T, isChecked: Boolean)

    fun appendListener(newListener: ICheckListener<T>) =
        ICheckListener<T> { model, value ->
            newListener.onCheckChange(model, value)
            this.onCheckChange(model, value)
        }
}