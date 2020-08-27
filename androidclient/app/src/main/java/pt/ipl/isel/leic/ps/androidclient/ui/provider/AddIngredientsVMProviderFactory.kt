package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.AddIngredientsListViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.IngredientListViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

open class AddIngredientsVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override val logger = Logger(AddIngredientsVMProviderFactory::class)

    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return if (modelClass == AddIngredientsListViewModel::class.java) {
            AddIngredientsListViewModel(
                navDestination = arguments?.getNavigation() ?: Navigation.IGNORE,
                actions = arguments?.getItemActions() ?: emptyList(),
                checkedItems = arguments?.getMealIngredients() ?: emptyList()
            )
        } else null
    }
}