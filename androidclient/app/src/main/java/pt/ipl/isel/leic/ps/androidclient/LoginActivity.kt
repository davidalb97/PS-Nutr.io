package pt.ipl.isel.leic.ps.androidclient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
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

        val userName: EditText = findViewById(R.id.userNameInput)
        val userPassword: EditText = findViewById(R.id.userPasswordInput)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.registerButton)
        val skipBtn = findViewById<Button>(R.id.skipButton)

        loginBtn.setOnClickListener {
            login(userName, userPassword)
        }

        registerBtn.setOnClickListener {
            register(userName, userPassword)
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
                ::saveSession
            )
            sharedPreferences.edit().putString("username", userNameParsed).apply()
            sharedPreferences.edit().putString("email", userNameParsed).apply()

            goToMainActivity()
        }
    }

    private fun register(username: EditText, password: EditText) {
        val userNameParsed = username.text.toString()
        val userPasswordParsed = password.text.toString()

        if (userPasswordParsed.isNotBlank() && userNameParsed.isNotBlank()) {
            viewModel.login(
                UserLogin(
                    username = userNameParsed,
                    password = userPasswordParsed
                ),
                ::saveSession
            )
            sharedPreferences.edit().putString("username", userNameParsed).apply()
            sharedPreferences.edit().putString("email", userNameParsed).apply()

            goToMainActivity()
        }
    }

    private fun skip() {
        sharedPreferences.edit().putBoolean("isFirstTime", false).apply()
        goToMainActivity()
    }

    private fun saveSession(userSession: UserSession) {
        sharedPreferences.edit().putString("jwt", userSession.jwt).apply()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}