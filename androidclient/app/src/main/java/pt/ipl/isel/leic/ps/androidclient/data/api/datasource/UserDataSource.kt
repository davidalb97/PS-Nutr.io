package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.LoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RegisterOutput

private const val LOGIN_URI = "$URI_BASE/login"
private const val LOGOUT_URI = "$URI_BASE/logout"
private const val REGISTER_URI = "$URI_BASE/register"

class UserDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {
    fun register(
        email: String,
        username: String,
        password: String,
        error: (VolleyError) -> Unit
    ) {

        requestParser.request(
            Method.POST,
            REGISTER_URI,
            RegisterOutput(
                email = email,
                username = username,
                password = password
            ),
            error,
            { }
        )
    }

    fun login(
        username: String,
        password: String,
        error: (VolleyError) -> Unit
    ) {

        requestParser.request(
            Method.POST,
            LOGIN_URI,
            LoginOutput(
                username = username,
                password = password
            ),
            error,
            { }
        )
    }

    fun logout() {

    }
}