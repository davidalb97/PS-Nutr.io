package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.getRestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.RestaurantInfoViewModel

class RestaurantInfoVMProviderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : BaseViewModelProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            RestaurantInfoViewModel::class.java -> RestaurantInfoViewModel().also { viewModel ->
                viewModel.restaurantId = requireNotNull(arguments?.getRestaurantItem()?.id) {
                    "Restaurant detail requires submission Id"
                }
            }
            else -> null
        }
    }
}