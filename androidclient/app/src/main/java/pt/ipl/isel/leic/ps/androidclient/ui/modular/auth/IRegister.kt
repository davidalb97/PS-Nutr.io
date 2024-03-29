package pt.ipl.isel.leic.ps.androidclient.ui.modular.auth

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.saveSession

interface IRegister : IUserInfo, ILoading, IContext, IRequiredTextInput, ILog {

    val userNameEditTextId: Int
    var userNameEditText: EditText
    val userPasswordEditTextId: Int
    var userPasswordEditText: EditText
    val userEmailEditTextId: Int
    var userEmailEditText: EditText
    val registerButtonId: Int
    var registerButton: Button

    fun setupRegister(view: View) {
        userNameEditText = view.findViewById(userNameEditTextId)
        userPasswordEditText = view.findViewById(userPasswordEditTextId)
        userEmailEditText = view.findViewById(userEmailEditTextId)
        registerButton = view.findViewById(registerButtonId)

        registerButton.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val password = userPasswordEditText.text.toString()
            val email = userEmailEditText.text.toString()

            if (validateTextViews(
                    fetchCtx(),
                    userNameEditText,
                    userPasswordEditText,
                    userEmailEditText
                )
            ) {
                startLoading()
                log.v("Registering...")
                onRegister(
                    userRegister = UserRegister(
                        email = email,
                        username = userName,
                        password = password
                    ),
                    onSuccess = { userSession ->
                        log.v("Registration complete! Fetching user info...")
                        onRequestUserInfo(
                            userSession = userSession,
                            onSuccess = { userInfo ->
                                log.v("Fetching user info completed!")
                                saveSession(
                                    jwt = userSession.jwt,
                                    email = userInfo.email,
                                    username = userInfo.username,
                                    password = password
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
                        log.v("Registration error!")
                        stopLoading()
                        Toast.makeText(
                            fetchCtx(),
                            R.string.register_error,
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

    fun onRegister(
        userRegister: UserRegister,
        onSuccess: (UserSession) -> Unit,
        onError: (Throwable) -> Unit
    )
}