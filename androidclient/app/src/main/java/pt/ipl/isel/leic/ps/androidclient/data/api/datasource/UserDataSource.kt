package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserLoginInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserRegisterInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.LoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RegisterOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.request.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

const val USER = "user"
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
    fun register(
        email: String,
        username: String,
        password: String,
        consumerDto: (UserRegisterInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.POST,
            uri = REGISTER_URI,
            reqPayload = RegisterOutput(
                email = email,
                username = username,
                password = password
            ),
            dtoClass = UserRegisterInput::class.java,
            onSuccess = consumerDto,
            onError = error
        )
    }

    fun login(
        username: String,
        password: String,
        error: (VolleyError) -> Unit,
        consumerDto: (UserLoginInput) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.POST,
            uri = LOGIN_URI,
            reqPayload = LoginOutput(
                email = username,
                password = password
            ),
            dtoClass = UserLoginInput::class.java,
            onSuccess = consumerDto,
            onError = error
        )
    }

    fun requestUserInfo(
        jwt: String,
        error: (VolleyError) -> Unit,
        consumerDto: (UserInfoInput) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = USER_INFO_URI,
            reqHeader = buildAuthHeader(jwt),
            dtoClass = UserInfoInput::class.java,
            onSuccess = consumerDto,
            onError = error
        )
    }

    /**
     * ################ GETs ################
     */

    fun getAllUserInsulinProfiles(
        userSession: UserSession
    ) {

        //var uri = "$INSULIN_URI$SUBMITTER_QUERY=${userSession.submitterId}"

        // Composing the authorization header
        val reqHeader = buildAuthHeader(userSession.jwt)

        /*requestParser.requestAndRespond(
            method = Method.GET,
            uri = INSULIN_URI,
            reqHeader = reqHeader,
            dtoClass = TODO(),
            onSuccess = TODO(),
            onError = TODO()
        )*/
    }

    fun getAllUserCustomMeals(
        userSession: UserSession
    ) {

    }

    fun getAllUserFavoriteMeals(
        userSession: UserSession
    ) {

    }

    fun getAllUserSubmissions(
        userSession: UserSession
    ) {

    }

    /**
     * ################ POSTs ################
     */

    fun postUserInsulinProfile(

    ) {

    }

    fun postUserCustomMeal(

    ) {

    }

    fun postUserFavoriteMeal(

    ) {

    }

    /**
     * ################ DELETEs ################
     */

    fun deleteUserInsulinProfile(

    ) {

    }

    fun deleteUserCustomMeal(

    ) {

    }

    fun deleteUserFavoriteMeal(

    ) {

    }

    private fun parseToDto(str: String): UserLoginInput {
        val pairs = str.split(",")

        val jwt = pairs[0].split(":")[1].removeSurrounding("\"")

        return UserLoginInput(
            jwt = jwt
        )
    }

}