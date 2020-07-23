package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.EMAIL
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.JWT
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.USERNAME
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireSharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserProfileViewModel

class LoginFragment : Fragment() {

    lateinit var viewModel: UserProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences

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
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireSharedPreferences()

        val signedUser = sharedPreferences.getString(USERNAME, null)

        val userNameEditText: EditText = view.findViewById(R.id.userNameInput)
        val userPasswordEditText: EditText = view.findViewById(R.id.userPasswordInput)
        val loginBtn = view.findViewById<Button>(R.id.loginButton)

        if (signedUser != null) {
            loginBtn.visibility = View.INVISIBLE
            userNameEditText.visibility = View.INVISIBLE
            userPasswordEditText.visibility = View.INVISIBLE

            val signedWarning = view.findViewById<TextView>(R.id.already_logged_in_warning)
            signedWarning.visibility = View.VISIBLE
            signedWarning.text = String.format(
                getString(R.string.already_logged_in_message),
                signedUser
            )

            val logoutButton = view.findViewById<Button>(R.id.logoutButton)
            logoutButton.visibility = View.VISIBLE

            // Eliminate shared preferences
            logoutButton.setOnClickListener {
                sharedPreferences.edit()
                    .clear()
                    .apply()
            }
        }

        loginBtn.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val password = userPasswordEditText.text.toString()
            if (listOf(userName, password).all(String::isNotBlank)) {
                viewModel.login(
                    UserLogin(
                        username = userName,
                        password = password
                    )
                ) { userSession ->
                    //Only save if login completed
                    saveSession(
                        jwt = userSession.jwt,
                        userName = userName,
                        email = userName
                    )
                }
            }
        }
    }

    private fun saveSession(jwt: String, userName: String, email: String) {
        sharedPreferences.edit()
            .putString(JWT, jwt)
            .putString(USERNAME, userName)
            .putString(EMAIL, email)
            .apply()
    }
}