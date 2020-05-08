package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantRecyclerViewModel

const val RESTAURANT_LIST_VIEW_STATE: String = "RESTAURANT_LIST_VIEW_STATE"

class RestaurantRecyclerVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<RestaurantRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = RESTAURANT_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<RestaurantRecyclerViewModel> =
        RestaurantRecyclerViewModel::class.java

    override fun newViewModel(): RestaurantRecyclerViewModel =
        RestaurantRecyclerViewModel()
}