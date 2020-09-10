package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getRestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddRestaurantViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel

class AddRestaurantVMProviderFactory(
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
            AddRestaurantViewModel::class.java -> AddRestaurantViewModel(
                editRestaurant = arguments?.getRestaurantInfo()
            )
            CuisinePickViewModel::class.java -> CuisinePickViewModel()
            else -> null
        }
    }
}