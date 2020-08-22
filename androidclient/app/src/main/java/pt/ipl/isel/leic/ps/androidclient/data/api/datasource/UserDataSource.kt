package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserLoginInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserRegisterInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.LoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RegisterOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

const val USER = "user"
private const val LOGIN_PATH = "login"
private const val REGISTER_PATH = "register"
private const val INFO_PATH = "info"
private const val LOGOUT_PATH = "logout"
private const val PROFILE_PATH = "profile"

private const val LOGIN_URI = "$URI_BASE/$USER/login"
private const val REGISTER_URI = "$URI_BASE/$USER/register"
private const val USER_INFO_URI = "$URI_BASE/$USER/info"
private const val LOGOUT_URI = "$URI_BASE/$USER/logout"
private const val INSULIN_URI = "$URI_BASE/$USER/profile"

class UserDataSource(
    private val requestParser: RequestParser
) {

    /**
     * ################ Auth methods ################
     */
    fun portRegister(
        registerOutput: RegisterOutput,
        consumerDto: (UserRegisterInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(USER)
                .appendPath(REGISTER_PATH)
                .build()
                .toString(),
            reqPayload = registerOutput,
            dtoClass = UserRegisterInput::class.java,
            onSuccess = consumerDto,
            onError = error
        )
    }

    fun postLogin(
        loginOutput: LoginOutput,
        error: (VolleyError) -> Unit,
        consumerDto: (UserLoginInput) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(USER)
                .appendPath(LOGIN_PATH)
                .build()
                .toString(),
            reqPayload = loginOutput,
            dtoClass = UserLoginInput::class.java,
            onSuccess = consumerDto,
            onError = error
        )
    }

    fun getUserInfo(
        jwt: String,
        error: (VolleyError) -> Unit,
        consumerDto: (UserInfoInput) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(USER)
                .appendPath(INFO_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            dtoClass = UserInfoInput::class.java,
            onSuccess = consumerDto,
            onError = error
        )
    }
}