package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction.CALCULATE
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction.DELETE
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

class CustomMealRecyclerVMProviderFactory(
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
            MealItemListViewModel::class.java -> MealItemListViewModel(
                navDestination = arguments?.getNavigation() ?: Navigation.SEND_TO_RESTAURANT_DETAIL,
                actions = arguments?.getItemActions() ?: listOf(DELETE, CALCULATE),
                source = Source.CUSTOM_MEAL
            )
            else -> null
        }
    }
}