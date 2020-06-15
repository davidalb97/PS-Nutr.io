package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.IngredientRecyclerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

const val INGREDIENT_LIST_VIEW_STATE: String = "INGREDIENT_LIST_VIEW_STATE"
const val BUNDLE_INGREDIENT_LIST: String = "BUNDLE_INGREDIENT_LIST"

class IngredientRecyclerVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<IngredientRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = MEAL_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<IngredientRecyclerViewModel> =
        IngredientRecyclerViewModel::class.java

    override fun newViewModel(): IngredientRecyclerViewModel = IngredientRecyclerViewModel()
}