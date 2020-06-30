package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.User
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserProfileViewModel

class SignFragment : Fragment() {

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
        return inflater.inflate(R.layout.sign_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName: EditText = view.findViewById(R.id.userNameInput)
        val userPassword: EditText = view.findViewById(R.id.userPasswordInput)
        val registerBtn = view.findViewById<Button>(R.id.registerButton)

        registerBtn.setOnClickListener {
            val userNameParsed = userName.text.toString()
            val userPasswordParsed = userPassword.text.toString()

            if (userPasswordParsed.isNotBlank() && userNameParsed.isNotBlank()) {
                viewModel.createUserProfile(
                    User(
                        userId = userPasswordParsed.toInt(),
                        name = userNameParsed
                    )
                ).setOnPostExecute {
                    Toast.makeText(
                        this.context,
                        "User with id $userPasswordParsed created",
                        Toast.LENGTH_LONG
                    ).show()
                }.execute()
            }
        }
    }
}