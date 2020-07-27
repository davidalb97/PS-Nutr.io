package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.saveSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.EMAIL
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.JWT
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.USERNAME
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserProfileViewModel

class RegisterFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: UserProfileViewModel

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = UserProfileVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[UserProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userEmailEditText: EditText = view.findViewById(R.id.userEmailInput)
        val userNameEditText: EditText = view.findViewById(R.id.userNameInput)
        val userPasswordEditText: EditText = view.findViewById(R.id.userPasswordInput)
        val registerBtn = view.findViewById<Button>(R.id.registerButton)
        viewModel.loadingCard = view.findViewById(R.id.loadingCard)

        registerBtn.setOnClickListener {
            val userEmail = userEmailEditText.text.toString()
            val userName = userNameEditText.text.toString()
            val userPassword = userPasswordEditText.text.toString()

            if (listOf(userEmail, userName, userPassword).all(String::isNotBlank)) {
                viewModel.startLoading()

                viewModel.register(
                    userRegister = UserRegister(
                        email = userEmail,
                        username = userName,
                        password = userPassword
                    ),
                    onError = {
                        viewModel.stopLoading()
                        Toast.makeText(context, R.string.register_error, Toast.LENGTH_SHORT).show()
                    }
                ) { userSession ->
                    saveSession(
                        jwt = userSession.jwt,
                        username = userName,
                        password = userPassword
                    )

                    statusMessage(getString(R.string.register_success))
                    view.findNavController().navigate(R.id.nav_home)
                }
            }
        }
    }

    private fun statusMessage(message: String) {
        viewModel.stopLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}