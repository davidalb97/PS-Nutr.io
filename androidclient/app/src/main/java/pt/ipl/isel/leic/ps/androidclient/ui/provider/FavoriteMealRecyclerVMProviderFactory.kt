package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

class FavoriteMealRecyclerVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override val logger = Logger(FavoriteMealRecyclerVMProviderFactory::class)

    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            MealItemListViewModel::class.java -> MealItemListViewModel(
                navDestination = arguments?.getNavigation() ?: Navigation.SEND_TO_RESTAURANT_DETAIL,
                actions = arguments?.getItemActions() ?: listOf(
                    ItemAction.DELETE,
                    ItemAction.CALCULATE
                ),
                source = Source.FAVORITE,
                checkedItems = arguments?.getMealItems() ?: emptyList()
            )
            else -> null
        }
    }
}
