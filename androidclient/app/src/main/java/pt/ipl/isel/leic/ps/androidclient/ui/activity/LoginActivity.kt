package pt.ipl.isel.leic.ps.androidclient.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.ILogin
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.IRegister
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.setIsFirstTime
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class LoginActivity : AppCompatActivity(), ILogin, IRegister, IViewModelManager {

    override val log: Logger by lazy { Logger(javaClass) }

    override val vMProviderFactorySupplier = ::UserProfileVMProviderFactory
    lateinit var viewModel: UserSessionViewModel

    //Loading
    override val loadingCardId: Int = R.id.loadingCard
    override lateinit var loadingCard: CardView

    //Register / Login
    override val userEmailEditTextId: Int = R.id.userEmailInput
    override lateinit var userEmailEditText: EditText
    override val userNameEditTextId = R.id.userNameInput
    override lateinit var userNameEditText: EditText
    override val userPasswordEditTextId = R.id.userPasswordInput
    override lateinit var userPasswordEditText: EditText

    //Login button
    override val loginButtonId: Int = R.id.loginButton
    override lateinit var loginButton: Button

    //Register button
    override val registerButtonId: Int = R.id.registerButton
    override lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = buildViewModel(
            arguments = null,
            intent = intent,
            savedInstanceState = savedInstanceState,
            vmClass = UserSessionViewModel::class.java
        )
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

    override fun onRequestUserInfo(
        userSession: UserSession,
        onSuccess: (UserInfo) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModel.requestUserInfo(
            userSession = userSession,
            onSuccess = onSuccess,
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