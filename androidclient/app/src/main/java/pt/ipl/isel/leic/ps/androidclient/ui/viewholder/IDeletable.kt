package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.widget.ImageButton
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

interface IDeletable<T> : ICardPressAction{

    var onDelete: (T) -> AsyncWorker<Unit, Unit>
    var deleteButton: ImageButton

}