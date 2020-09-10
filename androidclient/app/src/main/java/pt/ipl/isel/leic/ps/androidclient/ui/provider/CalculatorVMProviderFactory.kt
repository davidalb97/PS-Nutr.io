package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel

class CalculatorVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseAddMealRecyclerVMProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        val mealItem = arguments?.getMealItem()
            ?: arguments?.getMealIngredient()
            ?: arguments?.getMealInfo()

        return when (modelClass) {
            InsulinProfilesListViewModel::class.java -> {
                InsulinProfilesListViewModel(
                    argumentMeal = mealItem,
                    actions = emptyList()
                )
            }
            else -> super.newViewModel(modelClass)
        }
    }
}