package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.annotation.SuppressLint
import android.content.Context
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
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
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

        sharedPreferences =
            requireActivity().baseContext?.getSharedPreferences(
                "preferences.xml",
                Context.MODE_PRIVATE
            )!!

        val signedUser = sharedPreferences.getString("username", "")

        if (signedUser != null) {
            val signedWarning = view.findViewById<TextView>(R.id.already_logged_in_warning)
            signedWarning.visibility = View.VISIBLE
            signedWarning.text = "You are already logged in with the user $signedUser"
            // TODO extract resource string
        }

        val userName: EditText = view.findViewById(R.id.userNameInput)
        val userPassword: EditText = view.findViewById(R.id.userPasswordInput)
        val loginBtn = view.findViewById<Button>(R.id.loginButton)

        loginBtn.setOnClickListener {
            val userNameParsed = userName.text.toString()
            val userPasswordParsed = userPassword.text.toString()

            if (userPasswordParsed.isNotBlank() && userNameParsed.isNotBlank()) {
                viewModel.login(
                    UserLogin(
                        username = userNameParsed,
                        password = userPasswordParsed
                    ),
                    ::saveSession
                )
                sharedPreferences.edit().putString("username", userNameParsed).apply()
                sharedPreferences.edit().putString("email", userNameParsed).apply()
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveSession(userSession: UserSession) {
        sharedPreferences.edit().putString("jwt", userSession.jwt).apply();
        sharedPreferences.edit().putInt("submitterId", userSession.submitterId).apply();
    }
}