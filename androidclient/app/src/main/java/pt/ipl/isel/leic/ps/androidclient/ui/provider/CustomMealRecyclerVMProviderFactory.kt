package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CustomMealRecyclerViewModel

const val CUSTOM_MEAL_LIST_VIEW_STATE: String = "CUSTOM_MEAL_LIST_VIEW_STATE"

class CustomMealRecyclerVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<CustomMealRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = CUSTOM_MEAL_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<CustomMealRecyclerViewModel> =
        CustomMealRecyclerViewModel::class.java

    override fun newViewModel(): CustomMealRecyclerViewModel =
        CustomMealRecyclerViewModel()
}