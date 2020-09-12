package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.IAccountSettings
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.IRegister
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.deleteSession
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class RegisterFragment : BaseFragment(), IViewModelManager, IRegister, IAccountSettings {

    override val layout = R.layout.register_fragment

    override var savedInstanceState: Bundle? = null
    private lateinit var viewModel: UserSessionViewModel
    override val vMProviderFactorySupplier = ::UserProfileVMProviderFactory

    //Loading
    override val loadingCardId = R.id.loadingCard
    override lateinit var loadingCard: CardView

    //Register
    override val userEmailEditTextId = R.id.userEmailInput
    override lateinit var userEmailEditText: EditText
    override val userNameEditTextId = R.id.userNameInput
    override lateinit var userNameEditText: EditText
    override val userPasswordEditTextId = R.id.userPasswordInput
    override lateinit var userPasswordEditText: EditText
    override val registerButtonId = R.id.registerButton
    override lateinit var registerButton: Button

    //Logout
    override val nonLogoutViewId = R.id.registerBox
    override lateinit var nonLogoutView: ViewGroup
    override val logoutViewId = R.id.logoutBox
    override lateinit var logoutView: ViewGroup
    override val alreadyLoggedInTextViewId = R.id.already_logged_in_warning
    override lateinit var alreadyLoggedInTextView: TextView
    override val logoutButtonId = R.id.logoutButton
    override lateinit var logoutButton: Button

    //Remove account
    override lateinit var removeAccountView: ViewGroup
    override var removeAccountId: Int = R.id.deleteAccountBox
    override lateinit var removeAccountButton: Button
    override val removeAccountButtonId: Int = R.id.deleteAccountButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = buildViewModel(
            arguments = arguments,
            intent = requireActivity().intent,
            savedInstanceState = savedInstanceState,
            vmClass = UserSessionViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.setupLoading(view)
        super.setupRegister(view)
        super.setupLogout(view, requireContext())
    }

    private fun statusMessage(message: String) {
        stopLoading()
        Toast.makeText(app, message, Toast.LENGTH_SHORT).show()
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
                statusMessage(getString(R.string.register_success))
                requireView().findNavController().navigate(R.id.nav_home)
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

    override fun onDeleteAccount(
        userLogin: UserLogin,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // HTTP workaround - DELETE requests' payloads are not defined in the RFC
        viewModel.login(
            userLogin = userLogin,
            onSuccess = {
                viewModel.deleteAccount(
                    userSession = it,
                    onSuccess = {
                        deleteSession()
                        Toast.makeText(context, R.string.remove_account_success, Toast.LENGTH_SHORT)
                            .show()
                        requireView().findNavController().navigate(R.id.nav_home)
                    },
                    onError = onError
                )
            },
            onError = onError
        )
    }

    override fun fetchCtx(): Context = requireContext()
}