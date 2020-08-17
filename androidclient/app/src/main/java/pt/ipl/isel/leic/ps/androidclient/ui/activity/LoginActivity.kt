package pt.ipl.isel.leic.ps.androidclient.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILogin
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRegister
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.setIsFirstTime
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class LoginActivity : AppCompatActivity(), ILogin, IRegister {

    lateinit var viewModel: UserSessionViewModel
    override lateinit var loadingCard: CardView
    override lateinit var userEmailEditText: EditText
    override lateinit var userNameEditText: EditText
    override lateinit var userPasswordEditText: EditText
    override lateinit var loginButton: Button
    override lateinit var registerButton: Button

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val factory = UserProfileVMProviderFactory(null, savedInstanceState, intent)
        viewModel = ViewModelProvider(this, factory)[UserSessionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildViewModel(savedInstanceState)
        setContentView(R.layout.login_activity)

        loadingCard = findViewById(R.id.loadingCard)
        setupLoading()
        userEmailEditText = findViewById(R.id.userEmailInput)
        userNameEditText = findViewById(R.id.userNameInput)
        userPasswordEditText = findViewById(R.id.userPasswordInput)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
        super.setupLogin()
        super.setupRegister()

        val skipBtn = findViewById<Button>(R.id.skipButton)
        skipBtn.setOnClickListener {
            sendToMainActivity()
        }
    }

    override fun onRegister(
        userRegister: UserRegister,
        onSuccess: (UserSession) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModel.register(
            userRegister = userRegister,
            onSuccess = {
                onSuccess(it)
                sendToMainActivity()
            },
            onError = onError
        )
    }

    override fun onLogin(
        userLogin: UserLogin,
        onSuccess: (UserSession) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModel.login(
            userLogin = userLogin,
            onSuccess = {
                onSuccess(it)
                sendToMainActivity()
            },
            onError = onError
        )
    }

    private fun sendToMainActivity() {
        sharedPreferences.edit().setIsFirstTime(false).apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun fetchCtx(): Context = this
}