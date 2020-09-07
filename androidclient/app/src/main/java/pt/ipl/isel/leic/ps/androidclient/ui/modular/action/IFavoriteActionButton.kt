package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

import android.view.View
import android.widget.ImageButton
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Favorites
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.changeColor

interface IFavoriteActionButton : IContext, IAction, IUserSession, ILog {

    val favoriteButtonId: Int
    var favoriteButton: ImageButton

    fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun setupFavoriteButton(view: View, favorites: Favorites) {
        favoriteButton = view.findViewById(favoriteButtonId)

        if (!actions.contains(ItemAction.FAVORITE) || !favorites.isFavorable) {
            return
        }
        val ctx = fetchCtx()
        if (favorites.isFavorite) {
            favoriteButton.changeColor(ctx, R.color.colorYellow)
        }
        favoriteButton.setOnClickListener {
            ensureUserSession(ctx) {
                val favoriteNextState = !favorites.isFavorite
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
        log.v("Setting favorite to \"$isFavorite\"")
        if (isFavorite) {
            favoriteButton.changeColor(fetchCtx(), R.color.colorYellow)
        } else {
            favoriteButton.changeColor(fetchCtx(), R.color.colorBlack)
        }
    }
}