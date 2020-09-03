package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.content.Context
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession

interface IUserSession {

    fun ensureUserSession(
        ctx: Context,
        failConsumer: () -> Unit = {},
        consumer: (UserSession) -> Unit
    ) {
        val userSession = getUserSession()
        if (userSession != null) {
            consumer(userSession)
        } else {
            Toast.makeText(ctx, R.string.login_required, Toast.LENGTH_SHORT).show()
            failConsumer()
        }
    }
}