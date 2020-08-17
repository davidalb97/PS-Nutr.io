package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRegister
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class RegisterFragment : BaseFragment(), IRegister {

    private lateinit var viewModel: UserSessionViewModel
    override lateinit var loadingCard: CardView
    override lateinit var userEmailEditText: EditText
    override lateinit var userNameEditText: EditText
    override lateinit var userPasswordEditText: EditText
    override lateinit var registerButton: Button

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

        userEmailEditText = view.findViewById(R.id.userEmailInput)
        userNameEditText = view.findViewById(R.id.userNameInput)
        userPasswordEditText = view.findViewById(R.id.userPasswordInput)
        registerButton = view.findViewById(R.id.registerButton)
        super.setupRegister()
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

    override fun fetchCtx(): Context = requireContext()

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): UserProfileVMProviderFactory {
        return UserProfileVMProviderFactory(arguments, savedInstanceState, intent)
    }

    override fun getLayout() = R.layout.register_fragment

}