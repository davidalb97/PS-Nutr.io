package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.widget.ImageButton

interface IDeletable<T> : ICardPressAction{

    var onDelete: (T) -> Unit
    var deleteButton: ImageButton

}