package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class UserProfileVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            UserSessionViewModel::class.java -> UserSessionViewModel()
            else -> null
        }
    }
}