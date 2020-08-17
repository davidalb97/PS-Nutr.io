package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.encryptedSharedPreferences
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILogin
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUsername
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class LoginFragment : BaseFragment(), ILogin {

    override lateinit var userNameEditText: EditText
    override lateinit var userPasswordEditText: EditText
    override lateinit var loginButton: Button
    override lateinit var loadingCard: CardView

    private lateinit var viewModel: UserSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, UserSessionViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingCard = view.findViewById(R.id.loadingCard)
        super.setupLoading()

        userNameEditText = view.findViewById(R.id.userNameInput)
        userPasswordEditText = view.findViewById(R.id.userPasswordInput)
        loginButton = view.findViewById(R.id.loginButton)
        loadingCard = view.findViewById(R.id.loadingCard)
        super.setupLogin()

        setupLogout(view)
    }

    private fun setupSignedWarning(view: View, userName: String) {
        val signedWarning = view.findViewById<TextView>(R.id.already_logged_in_warning)
        signedWarning.text = String.format(
            getString(R.string.already_logged_in_message),
            userName
        )
    }

    private fun statusMessage(message: String) {
        stopLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearUserData() {
        encryptedSharedPreferences.edit()
            .clear()
            .apply()
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    private fun setupLogout(view: View) {
        val signedUser = encryptedSharedPreferences.getUsername() ?: return

        val loginBox = view.findViewById<RelativeLayout>(R.id.loginBox)
        val logoutBox = view.findViewById<RelativeLayout>(R.id.logoutBox)

        loginBox.visibility = View.GONE
        logoutBox.visibility = View.VISIBLE
        setupSignedWarning(view, signedUser)

        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            clearUserData()
            statusMessage(getString(R.string.logout_success))
            view.findNavController().navigate(R.id.nav_home)
        }
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
                statusMessage(getString(R.string.login_success))
                requireView().findNavController().navigate(R.id.nav_home)
            },
            onError = onError
        )
    }

    override fun fetchCtx(): Context = requireContext()

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): UserProfileVMProviderFactory {
        return UserProfileVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getLayout() = R.layout.login_fragment
}