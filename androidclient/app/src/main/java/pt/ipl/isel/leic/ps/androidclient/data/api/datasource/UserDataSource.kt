package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.Method
import pt.ipl.isel.leic.ps.androidclient.data.api.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.api.UriBuilder
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserLoginInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.LoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RegisterOutput

private const val USER = "user"
private const val LOGIN_URI = "$URI_BASE/$USER/login"
private const val REGISTER_URI = "$URI_BASE/$USER/register"
private const val LOGOUT_URI = "$URI_BASE/$USER/logout"
private const val INSULIN_URI = "$URI_BASE/$USER/profile"

class UserDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    /**
     * ################ Auth methods ################
     */
    fun register(
        email: String,
        username: String,
        password: String,
        error: (VolleyError) -> Unit
    ) {

        requestParser.request(
            method = Method.POST,
            urlStr = REGISTER_URI,
            reqPayload = RegisterOutput(
                email = email,
                username = username,
                password = password
            ),
            onError = error,
            responseConsumer = { }
        )
    }

    fun login(
        username: String,
        password: String,
        error: (VolleyError) -> Unit,
        consumerDto: (UserLoginInput) -> Unit
    ) {
        requestParser.request(
            method = Method.POST,
            urlStr = LOGIN_URI,
            reqPayload = LoginOutput(
                username = username,
                password = password
            ),
            onError = error,
            responseConsumer = { consumerDto(parseToDto(it)) }
        )
    }

    fun logout() {

    }

    /**
     * ################ GETs ################
     */

    fun getUserInsulinProfiles(

    ) {

    }

    fun getUserCustomMeals(

    ) {

    }

    fun getUserFavoriteMeals(

    ) {

    }

    fun getUserSubmissions(

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
        val submitterId = pairs[1].split(":")[1].removeSuffix("}").toInt()

        return UserLoginInput(
            jwt = jwt,
            submitterId = submitterId
        )
    }

}