package pt.ipl.isel.leic.ps.androidclient.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.login_activity.*
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.ILogin
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.IRegister
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.setIsFirstTime
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class LoginActivity : AppCompatActivity(), ILogin, IRegister {

    lateinit var viewModel: UserSessionViewModel

    //Loading
    override val loadingCardId: Int = R.id.loadingCard
    override lateinit var loadingCard: CardView

    //Register / Login
    override val userNameEditTextId = R.id.userNameInput
    override lateinit var userNameEditText: EditText
    override val userPasswordEditTextId = R.id.userPasswordInput
    override lateinit var userPasswordEditText: EditText

    //Login
    override val loginButtonId: Int = R.id.loginButton
    override lateinit var loginButton: Button

    //Register
    override val userEmailEditTextId: Int = R.id.userEmailInput
    override lateinit var userEmailEditText: EditText
    override val registerButtonId: Int = R.id.registerButton
    override lateinit var registerButton: Button

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val factory = UserProfileVMProviderFactory(null, savedInstanceState, intent)
        viewModel = ViewModelProvider(this, factory)[UserSessionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildViewModel(savedInstanceState)
        setContentView(R.layout.login_activity)

        val view: View = findViewById(R.id.login_activity_layout)
        setupLoading(view)
        super.setupLogin(view)
        super.setupRegister(view)

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