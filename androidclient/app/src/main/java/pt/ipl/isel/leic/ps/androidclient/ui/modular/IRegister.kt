package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.saveSession

interface IRegister : ILoading, IContext {

    val userNameEditText: EditText
    val userPasswordEditText: EditText
    val userEmailEditText: EditText
    val registerButton: Button

    fun setupRegister() {
        registerButton.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val password = userPasswordEditText.text.toString()
            val email = userEmailEditText.text.toString()

            if (arrayOf(userName, password, email).all { it.isNotBlank() }) {
                startLoading()
                onRegister(
                    userRegister = UserRegister(
                        email = email,
                        username = userName,
                        password = password
                    ),
                    onSuccess = { userSession ->
                        saveSession(
                            jwt = userSession.jwt,
                            username = userName,
                            password = password
                        )
                        stopLoading()
                    },
                    onError = {
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