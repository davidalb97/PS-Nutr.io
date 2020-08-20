package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getMealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddCustomMealViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.IngredientPickViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.MealInfoPickViewModel

class AddCustomMealRecyclerVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override val logger = Logger(AddCustomMealRecyclerVMProviderFactory::class)

    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            AddCustomMealViewModel::class.java -> AddCustomMealViewModel(
                editMeal = arguments?.getMealInfo()
            )
            CuisinePickViewModel::class.java -> CuisinePickViewModel()
            MealInfoPickViewModel::class.java -> MealInfoPickViewModel()
            IngredientPickViewModel::class.java -> IngredientPickViewModel()
            else -> null
        }
    }
}