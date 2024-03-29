package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealIngredients
import pt.ipl.isel.leic.ps.androidclient.ui.util.getNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.IngredientListViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

open class IngredientRecyclerVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return if (modelClass == IngredientListViewModel::class.java
            || modelClass == MealItemListViewModel::class.java
        ) {
            IngredientListViewModel(
                navDestination = arguments?.getNavigation() ?: Navigation.IGNORE,
                actions = arguments?.getItemActions() ?: emptyList()
            ).also { viewModel ->
                val mealIngredients = arguments?.getMealIngredients()
                if (mealIngredients != null) {
                    viewModel.restoreFromList(mealIngredients)
                }
            }
        } else null
    }
}