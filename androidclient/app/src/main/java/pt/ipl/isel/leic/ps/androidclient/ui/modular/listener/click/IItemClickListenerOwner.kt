package pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click

interface IItemClickListenerOwner<T : Any> {

    var onClickListener: IItemClickListener<T>?
}