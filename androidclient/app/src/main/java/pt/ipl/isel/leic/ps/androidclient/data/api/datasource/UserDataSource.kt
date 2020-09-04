package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.UserInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.UserLoginInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.UserRegisterInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.LoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RegisterOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser

const val USER = "user"
private const val LOGIN_PATH = "login"
private const val REGISTER_PATH = "register"

class UserDataSource(
    private val requestParser: RequestParser
) {

    /**
     * ################ Auth methods ################
     */
    fun postRegister(
        registerOutput: RegisterOutput,
        consumerDto: (UserRegisterInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
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
                .encodedAuthority(ADDRESS_PORT)
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
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(USER)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            dtoClass = UserInfoInput::class.java,
            onSuccess = consumerDto,
            onError = error
        )
    }

    fun deleteAccount(
        jwt: String,
        loginOutput: LoginOutput,
        onSuccess: () -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        requestParser.request(
            method = HTTPMethod.DELETE,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(USER)
                .build()
                .toString(),
            reqPayload = loginOutput,
            onError = onError,
            responseConsumer = { onSuccess() }
        )
    }
}