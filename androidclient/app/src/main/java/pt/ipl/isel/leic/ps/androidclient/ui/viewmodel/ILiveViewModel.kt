package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem

interface ILiveViewModel<T> {

    fun updateLiveListExchangingLiveData(
        onSuccess: (List<RestaurantItem>) -> Unit,
        onError: () -> Unit
    )

    fun updateLiveList(
        onSuccess: (List<RestaurantItem>) -> Unit,
        onError: () -> Unit
    ): LiveData<List<T>>?
}