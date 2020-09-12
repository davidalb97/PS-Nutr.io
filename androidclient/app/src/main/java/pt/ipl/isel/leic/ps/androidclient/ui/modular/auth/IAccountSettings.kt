package pt.ipl.isel.leic.ps.androidclient.ui.modular.auth

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRequiredTextInput
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.deleteSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUsername

interface IAccountSettings : IRequiredTextInput, ILog {

    val nonLogoutViewId: Int
    var nonLogoutView: ViewGroup
    val logoutViewId: Int
    var logoutView: ViewGroup
    var removeAccountView: ViewGroup
    var removeAccountId: Int
    val removeAccountButtonId: Int
    var removeAccountButton: Button
    val alreadyLoggedInTextViewId: Int
    var alreadyLoggedInTextView: TextView
    val logoutButtonId: Int
    var logoutButton: Button

    fun setupLogout(view: View, context: Context) {

        nonLogoutView = view.findViewById(nonLogoutViewId)
        removeAccountView = view.findViewById(removeAccountId)
        logoutView = view.findViewById(logoutViewId)
        alreadyLoggedInTextView = view.findViewById(alreadyLoggedInTextViewId)

        setupSignedWarning(context)
        setupRemoveAccountButton(view, context)
        setupLogoutButton(view, context)
    }

    private fun setupSignedWarning(context: Context) {
        val signedUser = NutrioApp.encryptedSharedPreferences.getUsername() ?: return
        nonLogoutView.visibility = View.GONE
        removeAccountView.visibility = View.VISIBLE
        logoutView.visibility = View.VISIBLE

        alreadyLoggedInTextView.text = String.format(
            context.resources.getString(R.string.already_logged_in_message),
            signedUser
        )
    }

    private fun setupRemoveAccountButton(view: View, context: Context) {
        val email: EditText = view.findViewById(R.id.deleteUserEmailInput)
        val password: EditText = view.findViewById(R.id.deleteUserPasswordInput)
        removeAccountButton = view.findViewById(removeAccountButtonId)

        removeAccountButton.setOnClickListener {
            if (!validateTextViews(context, email, password)) return@setOnClickListener
            val emailStr = email.text.toString()
            val passwordStr = password.text.toString()

            onDeleteAccount(
                userLogin = UserLogin(email = emailStr, password = passwordStr),
                onSuccess = {
                    Toast.makeText(context, R.string.remove_account_success, Toast.LENGTH_SHORT)
                        .show()
                    view.findNavController().navigate(Navigation.SEND_TO_HOME.navId)
                },
                onError = {
                    Toast.makeText(context, R.string.remove_account_error, Toast.LENGTH_SHORT)
                        .show()
                })
        }
    }

    private fun setupLogoutButton(view: View, context: Context) {
        logoutButton = view.findViewById(logoutButtonId)
        logoutButton.setOnClickListener {
            log.v("Logging out...")
            deleteSession()
            Toast.makeText(context, R.string.logout_success, Toast.LENGTH_SHORT).show()
            log.v("Logout completed!")
            view.findNavController().navigate(Navigation.SEND_TO_HOME.navId)
        }
    }

    fun onDeleteAccount(
        userLogin: UserLogin,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    )
}