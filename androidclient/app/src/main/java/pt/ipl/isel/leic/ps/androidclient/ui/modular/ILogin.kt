package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.saveSession

interface ILogin : ILoading, IContext {

    val userNameEditText: EditText
    val userPasswordEditText: EditText
    val loginButton: Button

    fun setupLogin() {
        loginButton.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val passWord = userPasswordEditText.text.toString()

            if (arrayOf(userName, passWord).all { it.isNotBlank() }) {
                startLoading()
                onLogin(
                    userLogin = UserLogin(
                        userName = userName,
                        passWord = passWord
                    ),
                    onSuccess = { userSession ->
                        saveSession(
                            jwt = userSession.jwt,
                            username = userName,
                            password = passWord
                        )
                        stopLoading()
                    },
                    onError = {
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