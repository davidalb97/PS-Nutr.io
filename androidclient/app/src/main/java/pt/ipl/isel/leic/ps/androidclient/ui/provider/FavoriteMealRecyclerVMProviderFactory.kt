package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.FavoriteMealRecyclerViewModel

const val FAVORITE_MEAL_LIST_VIEW_STATE: String = "FAVORITE_MEAL_LIST_VIEW_STATE"

class FavoriteMealRecyclerVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<FavoriteMealRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = FAVORITE_MEAL_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<FavoriteMealRecyclerViewModel> =
        FavoriteMealRecyclerViewModel::class.java

    override fun newViewModel(): FavoriteMealRecyclerViewModel =
        FavoriteMealRecyclerViewModel()
}
