package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.MealInfoViewModel

class CalculatorVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override val logger = Logger(CalculatorVMProviderFactory::class)

    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            MealInfoViewModel::class.java -> {
                val mealInfo = arguments?.getMealInfo()
                val mealItem = arguments?.getMealItem()
                when {
                    mealInfo != null -> MealInfoViewModel(
                        mealInfo = mealInfo,
                        ingredientActions = emptyList()
                    )
                    mealItem != null -> MealInfoViewModel(
                        mealItem = mealItem,
                        ingredientActions = emptyList()
                    )
                    else -> MealInfoViewModel(
                        ingredientActions = emptyList()
                    )
                }
            }
            InsulinProfilesListViewModel::class.java -> {
                InsulinProfilesListViewModel(
                    actions = emptyList()
                )
            }
            else -> null
        }
    }
}