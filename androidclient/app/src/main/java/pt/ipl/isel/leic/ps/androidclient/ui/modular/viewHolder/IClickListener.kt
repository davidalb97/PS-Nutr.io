package pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder

import android.view.View

interface IClickListener<T : Any> {

    var onClickListener: ((v: View) -> Unit)?
}