package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.getItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.MealInfoViewModel

class MealInfoVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : IngredientRecyclerVMProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
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
                    else -> throw UnsupportedOperationException("No meal passed to info ViewModel!")
                }
            }
            else -> super.newViewModel(modelClass)
        }
    }
}