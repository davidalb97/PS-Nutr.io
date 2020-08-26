package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check

interface ICheckListenerOwner<T> {

    var onCheckListener: ICheckListener<T>?

}