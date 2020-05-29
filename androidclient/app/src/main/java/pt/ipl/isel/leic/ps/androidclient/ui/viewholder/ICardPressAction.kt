package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.view.View

interface ICardPressAction:
    View.OnClickListener,
    View.OnLongClickListener {

    fun turnVisible()
    fun turnInvisible()
}