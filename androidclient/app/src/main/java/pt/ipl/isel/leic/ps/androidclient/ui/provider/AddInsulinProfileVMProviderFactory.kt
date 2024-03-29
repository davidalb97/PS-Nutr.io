package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddInsulinProfileViewModel

open class AddInsulinProfileVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : InsulinProfilesVMProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            AddInsulinProfileViewModel::class.java -> AddInsulinProfileViewModel()
            else -> super.newViewModel(modelClass)
        }
    }
}