package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel

class InsulinProfilesVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override val logger = Logger(InsulinProfilesVMProviderFactory::class)

    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            InsulinProfilesListViewModel::class.java -> {
                InsulinProfilesListViewModel(
                    actions = arguments?.getItemActions() ?: emptyList()
                )
            }
            else -> null
        }
    }
}