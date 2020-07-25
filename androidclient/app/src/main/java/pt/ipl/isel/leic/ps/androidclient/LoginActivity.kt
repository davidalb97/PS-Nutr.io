package pt.ipl.isel.leic.ps.androidclient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.EMAIL
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

        loginBtn.setOnClickListener {
            login(userName, userPassword)
        }

        registerBtn.setOnClickListener {
            register(userEmail, userName, userPassword)
        }

        skipBtn.setOnClickListener {
            skip()
        }
    }

    private fun login(username: EditText, password: EditText) {
        val userNameParsed = username.text.toString()
        val userPasswordParsed = password.text.toString()

        if (userPasswordParsed.isNotBlank() && userNameParsed.isNotBlank()) {
            viewModel.login(
                UserLogin(
                    username = userNameParsed,
                    password = userPasswordParsed
                ),
                onError = {
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                },
                userSessionConsumer = {
                    saveSession(
                        jwt = it.jwt,
                        userName = userNameParsed,
                        email = userNameParsed
                    )
                    skip()
                }
            )
        }
    }

    private fun register(email: EditText, username: EditText, password: EditText) {
        val userEmailParsed = email.text.toString()
        val userNameParsed = username.text.toString()
        val userPasswordParsed = password.text.toString()

        if (userPasswordParsed.isNotBlank() && userNameParsed.isNotBlank()) {
            viewModel.login(
                UserLogin(
                    username = userNameParsed,
                    password = userPasswordParsed
                ),
                onError = {
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                },
                userSessionConsumer = {
                    saveSession(
                        jwt = it.jwt,
                        userName = userNameParsed,
                        email = userEmailParsed
                    )
                    skip()
                }
            )

            goToMainActivity()
        }
    }

    private fun saveSession(jwt: String, userName: String, email: String) {
        sharedPreferences.edit()
            .putString(JWT, jwt)
            .putString(USERNAME, userName)
            .putString(EMAIL, email)
            .apply()
    }

    private fun statusMessage(message: String) {
        viewModel.progressWheel.visibility = View.INVISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun skip() {
        sharedPreferences.edit().putBoolean("isFirstTime", false).apply()
        goToMainActivity()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}