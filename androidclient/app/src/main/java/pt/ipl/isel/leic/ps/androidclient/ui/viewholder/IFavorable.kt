package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.widget.ImageButton

interface IFavorable<T> : ICardPressAction {

    var onFavorite: (T) -> Unit
    var favoriteButton: ImageButton

}