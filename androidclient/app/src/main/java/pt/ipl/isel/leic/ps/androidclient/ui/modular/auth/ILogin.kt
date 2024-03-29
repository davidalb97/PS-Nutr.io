package pt.ipl.isel.leic.ps.androidclient.ui.modular.auth

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.saveSession

interface ILogin : IUserInfo, ILoading, IContext, IRequiredTextInput, ILog {

    var userEmailEditText: EditText
    val userEmailEditTextId: Int
    var userPasswordEditText: EditText
    val userPasswordEditTextId: Int
    var loginButton: Button
    val loginButtonId: Int

    fun setupLogin(view: View) {

        userEmailEditText = view.findViewById(userEmailEditTextId)
        userPasswordEditText = view.findViewById(userPasswordEditTextId)
        loginButton = view.findViewById(loginButtonId)

        loginButton.setOnClickListener {
            val userEmail = userEmailEditText.text.toString()
            val passWord = userPasswordEditText.text.toString()

            if (validateTextViews(fetchCtx(), userEmailEditText, userPasswordEditText)) {
                startLoading()
                log.v("Logging in...")
                onLogin(
                    userLogin = UserLogin(
                        email = userEmail,
                        password = passWord
                    ),
                    onSuccess = { userSession ->
                        log.v("Logging in completed! Fetching user info...")
                        onRequestUserInfo(
                            userSession = userSession,
                            onSuccess = { userInfo ->
                                log.v("Fetching user info completed!")
                                saveSession(
                                    jwt = userSession.jwt,
                                    email = userInfo.email,
                                    username = userInfo.username,
                                    password = passWord
                                )
                            },
                            onError = {
                                log.v("Fetching user info error!")
                                stopLoading()
                                Toast.makeText(
                                    fetchCtx(),
                                    R.string.user_info_error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        stopLoading()
                    },
                    onError = {
                        log.v("Login error!")
                        stopLoading()
                        Toast.makeText(
                            fetchCtx(),
                            R.string.login_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            } else {
                Toast.makeText(
                    fetchCtx(),
                    R.string.Fill_in_all_the_available_fields,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onLogin(
        userLogin: UserLogin,
        onSuccess: (UserSession) -> Unit,
        onError: (Throwable) -> Unit
    )
}