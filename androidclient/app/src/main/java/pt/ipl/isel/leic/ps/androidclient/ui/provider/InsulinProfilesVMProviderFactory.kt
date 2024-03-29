package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.getItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel

open class InsulinProfilesVMProviderFactory(
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
            InsulinProfilesListViewModel::class.java -> {
                InsulinProfilesListViewModel(
                    actions = arguments?.getItemActions() ?: listOf(
                        ItemAction.DELETE,
                        ItemAction.ADD
                    )
                )
            }
            else -> null
        }
    }
}