package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.IAccountSettings
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.ILogin
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class LoginFragment : BaseFragment(), IViewModelManager, ILogin, IAccountSettings {

    override val layout = R.layout.login_fragment
    override var savedInstanceState: Bundle? = null
    override val vMProviderFactorySupplier = ::UserProfileVMProviderFactory
    private lateinit var viewModel: UserSessionViewModel

    //Loading
    override val loadingCardId = R.id.loadingCard
    override lateinit var loadingCard: CardView

    //Login
    override var userEmailEditTextId = R.id.userEmailInput
    override lateinit var userEmailEditText: EditText
    override val userPasswordEditTextId = R.id.userPasswordInput
    override lateinit var userPasswordEditText: EditText
    override val loginButtonId = R.id.loginButton
    override lateinit var loginButton: Button

    //Logout
    override val nonLogoutViewId = R.id.loginBox
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
        super.setupLogin(view)
        super.setupLogout(view, requireContext())
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
                Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show()
                requireView().findNavController().navigate(R.id.nav_home)
            },
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

    override fun fetchCtx(): Context = requireContext()
}