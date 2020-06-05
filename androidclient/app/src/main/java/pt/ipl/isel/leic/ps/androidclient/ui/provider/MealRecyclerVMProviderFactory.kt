package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ApiMealRecyclerViewModel

const val MEAL_LIST_VIEW_STATE: String = "MEAL_LIST_VIEW_STATE"

class MealRecyclerVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<ApiMealRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = MEAL_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<ApiMealRecyclerViewModel> =
        ApiMealRecyclerViewModel::class.java

    override fun newViewModel(): ApiMealRecyclerViewModel =
        ApiMealRecyclerViewModel(TODO("Must read state and pass restaurantId") as Int)
}