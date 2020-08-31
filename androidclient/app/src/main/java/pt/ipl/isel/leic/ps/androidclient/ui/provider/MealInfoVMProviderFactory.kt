package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.MealInfoViewModel

open class MealInfoVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : IngredientRecyclerVMProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override val logger = Logger(MealInfoVMProviderFactory::class)

    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            MealInfoViewModel::class.java -> {
                val mealInfo = arguments?.getMealInfo()
                val mealItem = arguments?.getMealItem() ?: arguments?.getMealIngredient()
                when {
                    mealInfo != null -> MealInfoViewModel(
                        mealInfo = mealInfo,
                        ingredientActions = arguments?.getItemActions() ?: emptyList()
                    )
                    mealItem != null -> MealInfoViewModel(
                        mealItem = mealItem,
                        ingredientActions = arguments?.getItemActions() ?: emptyList()
                    )
                    else -> MealInfoViewModel(
                        ingredientActions = arguments?.getItemActions() ?: emptyList()
                    )
                }
            }
            else -> super.newViewModel(modelClass)
        }
    }
}