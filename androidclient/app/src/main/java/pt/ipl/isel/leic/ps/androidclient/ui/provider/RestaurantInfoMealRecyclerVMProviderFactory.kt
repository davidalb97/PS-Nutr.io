package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantInfoMealRecyclerViewModel

const val RESTAURANT_INFO_VIEW_STATE: String = "RESTAURANT_INFO_VIEW_STATE"
const val BUNDLE_RESTAURANT_INFO_ID = "BUNDLE_RESTAURANT_INFO_ID"

class RestaurantInfoVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<RestaurantInfoMealRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = RESTAURANT_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<RestaurantInfoMealRecyclerViewModel> =
        RestaurantInfoMealRecyclerViewModel::class.java

    override fun newViewModel(): RestaurantInfoMealRecyclerViewModel =
        RestaurantInfoMealRecyclerViewModel(
            restaurantId = savedInstanceState!!.getString(BUNDLE_RESTAURANT_INFO_ID)!!
        )
}