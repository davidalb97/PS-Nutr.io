package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction.CALCULATE
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction.DELETE
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.util.getNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealInfoListViewModel

class CustomMealRecyclerVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override val logger = Logger(CustomMealRecyclerVMProviderFactory::class)

    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            MealInfoListViewModel::class.java -> MealInfoListViewModel(
                navDestination = arguments?.getNavigation() ?: Navigation.SEND_TO_RESTAURANT_DETAIL,
                actions = arguments?.getItemActions() ?: listOf(DELETE, CALCULATE),
                source = Source.CUSTOM
            )
            else -> null
        }
    }
}