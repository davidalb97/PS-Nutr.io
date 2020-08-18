package pt.ipl.isel.leic.ps.androidclient.ui.modular

import pt.ipl.isel.leic.ps.androidclient.data.model.UserInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

interface IUserInfo {

    fun onRequestUserInfo(
        userSession: UserSession,
        onSuccess: (UserInfo) -> Unit,
        onError: (Throwable) -> Unit
    )
}