package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check

fun interface ICheckListener<T> {

    fun onCheckChange(item: T, isChecked: Boolean, onChangeCallback: () -> Unit)

    fun appendListener(newListener: ICheckListener<T>) =
        ICheckListener<T> { model, value, onChangeCallback ->
            newListener.onCheckChange(model, value, onChangeCallback)
            this.onCheckChange(model, value, onChangeCallback)
        }
}