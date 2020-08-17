package pt.ipl.isel.leic.ps.androidclient.ui.modular.auth

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.deleteSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUsername

interface ILogout {

    val nonLogoutViewId: Int
    var nonLogoutView: ViewGroup
    val logoutViewId: Int
    var logoutView: ViewGroup
    val alreadyLoggedInTextViewId: Int
    var alreadyLoggedInTextView: TextView
    val logoutButtonId: Int
    var logoutButton: Button

    fun setupLogout(view: View, context: Context) {

        nonLogoutView = view.findViewById(nonLogoutViewId)
        logoutView = view.findViewById(logoutViewId)
        alreadyLoggedInTextView = view.findViewById(alreadyLoggedInTextViewId)

        setupSignedWarning(context)
        setupLogoutButton(view, context)
    }

    private fun setupSignedWarning(context: Context) {
        val signedUser = NutrioApp.encryptedSharedPreferences.getUsername() ?: return
        nonLogoutView.visibility = View.GONE
        logoutView.visibility = View.VISIBLE

        alreadyLoggedInTextView.text = String.format(
            context.resources.getString(R.string.already_logged_in_message),
            signedUser
        )
    }

    private fun setupLogoutButton(view: View, context: Context) {
        logoutButton = view.findViewById(logoutButtonId)
        logoutButton.setOnClickListener {
            deleteSession()
            Toast.makeText(context, R.string.logout_success, Toast.LENGTH_SHORT)
                .show()
            view.findNavController().navigate(Navigation.SEND_TO_HOME.navId)
        }
    }
}