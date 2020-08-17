package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

interface IActionEvent<P> {

    fun onActionEvent(vararg param: P, onFinishConsumer: () -> Unit)
}