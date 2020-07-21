package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.widget.ImageButton

interface ICalculatable<T> : ICardPressAction {

    var calculatorButton: ImageButton
}