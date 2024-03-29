package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

open class MealRecyclerVMProviderFactory(
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
            MealItemListViewModel::class.java -> {
                MealItemListViewModel(
                    navDestination = arguments?.getNavigation()
                        ?: Navigation.SEND_TO_MEAL_DETAIL,
                    actions = arguments?.getItemActions() ?: emptyList(),
                    source = requireNotNull(arguments?.getSource()),
                    restaurantId = arguments?.getRestaurantSubmissionId(),
                    cuisines = arguments?.getCuisines() ?: emptyList()
                )
            }
            else -> null
        }
    }
}