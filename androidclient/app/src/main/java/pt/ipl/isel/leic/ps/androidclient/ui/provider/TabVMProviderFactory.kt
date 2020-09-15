package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction.CALCULATE
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction.DELETE
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.util.getNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.TabViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

open class TabVMProviderFactory(
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
            TabViewModel::class.java -> TabViewModel()
            else -> null
        }
    }
}