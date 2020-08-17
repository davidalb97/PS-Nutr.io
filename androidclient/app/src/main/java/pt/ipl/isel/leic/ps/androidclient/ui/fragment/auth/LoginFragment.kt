package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.content.Context
import android.content.Intent
import android.content.LocusId
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
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.ILogin
import pt.ipl.isel.leic.ps.androidclient.ui.modular.auth.ILogout
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUsername
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class LoginFragment : BaseFragment(), ILogin, ILogout {

    private lateinit var viewModel: UserSessionViewModel

    //Loading
    override val loadingCardId = R.id.loadingCard
    override lateinit var loadingCard: CardView

    //Login
    override var userNameEditTextId = R.id.userNameInput
    override lateinit var userNameEditText: EditText
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