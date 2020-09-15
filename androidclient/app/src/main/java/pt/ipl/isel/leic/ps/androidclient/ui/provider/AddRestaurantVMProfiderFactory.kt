package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.RestaurantInfoViewModel

class AddRestaurantVMProfiderFactory(
    arguments: Bundle?,
    savedInstanceState: Bundle?,
    intent: Intent
) : TabVMProviderFactory(
    arguments,
    savedInstanceState,
    intent
) {
    override fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel? {
        return when (modelClass) {
            RestaurantInfoViewModel::class.java -> RestaurantInfoViewModel()
            else -> super.newViewModel(modelClass)
        }
    }
}