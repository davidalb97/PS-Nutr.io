package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.AddCustomMealViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel

class AddCustomMealRecyclerVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseAddMealRecyclerVMProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            AddCustomMealViewModel::class.java -> {
                val mealInfo = arguments?.getMealInfo()
                val mealItem = arguments?.getMealItem() ?: arguments?.getMealIngredient()
                when {
                    mealInfo != null -> AddCustomMealViewModel(
                        mealInfo = mealInfo
                    )
                    mealItem != null -> AddCustomMealViewModel(
                        mealItem = mealItem
                    )
                    else -> AddCustomMealViewModel()
                }
            }
            CuisinePickViewModel::class.java -> CuisinePickViewModel()
            else -> super.newViewModel(modelClass)
        }
    }
}