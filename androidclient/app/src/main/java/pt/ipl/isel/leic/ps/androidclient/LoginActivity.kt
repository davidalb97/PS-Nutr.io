package pt.ipl.isel.leic.ps.androidclient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.EMAIL
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.FIRST_TIME
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.JWT
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.USERNAME
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserProfileViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: UserProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val factory = UserProfileVMProviderFactory(savedInstanceState, intent)
        viewModel = ViewModelProvider(this, factory)[UserProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            this.baseContext?.getSharedPreferences(
                "preferences.xml",
                Context.MODE_PRIVATE
            )!!

        buildViewModel(savedInstanceState)
        setContentView(R.layout.login_activity)

        val userEmail: EditText = findViewById(R.id.userEmailInput)
        val userName: EditText = findViewById(R.id.userNameInput)
        val userPassword: EditText = findViewById(R.id.userPasswordInput)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.registerButton)
        val skipBtn = findViewById<Button>(R.id.skipButton)
        viewModel.loadingCard = findViewById(R.id.loadingCard)

        val signButtons = arrayOf(loginBtn, registerBtn)

        signButtons.forEach { signButton ->
            signButton.setOnClickListener {
                sign(userEmail, userName, userPassword)
            }
        }

        skipBtn.setOnClickListener {
            skip()
        }
    }

    private fun sign(email: EditText, username: EditText, password: EditText) {
        val userEmailParsed = email.text.toString()
        val userNameParsed = username.text.toString()
        val userPasswordParsed = password.text.toString()

        val credentials = arrayOf(userEmailParsed, userNameParsed, userPasswordParsed)
        val loginCredentials = arrayOf(userNameParsed, userPasswordParsed)

        if (credentials.all { it.isNotBlank() }) {
            register(userEmailParsed, userNameParsed, userPasswordParsed)
            viewModel.startLoading()
        } else if (userEmailParsed.isBlank() && loginCredentials.all { it.isNotBlank() }) {
            login(userNameParsed, userPasswordParsed)
            viewModel.startLoading()
        } else {
            Toast.makeText(this, R.string.Fill_in_all_the_available_fields, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun login(username: String, password: String) {
        viewModel.login(
            UserLogin(
                username = username,
                password = password
            ),
            onError = {
                viewModel.stopLoading()
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
            },
            userSessionConsumer = { userSession ->
                saveSession(
                    jwt = userSession.jwt,
                    userName = username,
                    email = username
                )
                skip()
            }
        )
    }

    private fun register(email: String, username: String, password: String) {
        viewModel.register(
            UserRegister(
                email = email,
                username = username,
                password = password
            ),
            onError = {
                viewModel.stopLoading()
                Toast.makeText(this, R.string.register_error, Toast.LENGTH_SHORT).show()
            },
            userSessionConsumer = { userSession ->
                saveSession(
                    jwt = userSession.jwt,
                    userName = username,
                    email = email
                )
                skip()
            }
        )
    }

    private fun saveSession(jwt: String, userName: String, email: String) {
        sharedPreferences.edit()
            .putString(JWT, jwt)
            .putString(USERNAME, userName)
            .putString(EMAIL, email)
            .apply()
    }

    private fun skip() {
        viewModel.stopLoading()
        sharedPreferences.edit().putBoolean(FIRST_TIME, false).apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}