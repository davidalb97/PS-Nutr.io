package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.widget.ImageButton
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

interface IFavorable<T> : ICardPressAction {

    var onFavorite: (T) -> AsyncWorker<Unit, Unit>
    var favoriteButton: ImageButton

}