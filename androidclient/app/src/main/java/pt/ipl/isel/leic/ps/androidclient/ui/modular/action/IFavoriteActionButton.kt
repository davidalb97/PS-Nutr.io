package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

import android.view.View
import android.widget.ImageButton
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.changeColor

interface IFavoriteActionButton : IContext, IAction, IUserSession, ILog {

    val favoriteButtonId: Int
    var favoriteButton: ImageButton

    fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun setupFavoriteButton(view: View) {
        favoriteButton = view.findViewById(favoriteButtonId)

        if (!actions.contains(ItemAction.FAVORITE)) {
            return
        }
        val ctx = fetchCtx()
        if (isFavorite()) {
            favoriteButton.changeColor(ctx, R.color.colorYellow)
        }
        favoriteButton.setOnClickListener {
            ensureUserSession(ctx) {
                val favoriteNextState = !isFavorite()
                onFavorite(
                    onSuccess = {
                        onItemFavorable(favoriteNextState)
                    },
                    onError = log::e
                )
            }
        }
        favoriteButton.visibility = View.VISIBLE
    }

    fun onItemFavorable(isFavorite: Boolean) {
        if (isFavorite) {
            favoriteButton.changeColor(fetchCtx(), R.color.colorYellow)
        } else {
            favoriteButton.changeColor(fetchCtx(), R.color.colorBlack)
        }
    }

    fun isFavorite(): Boolean
}