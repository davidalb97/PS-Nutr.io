package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem

interface IStaticViewModel<T> {

    fun updateListExchangingLiveData(
        onSuccess: (List<RestaurantItem>) -> Unit,
        onError: () -> Unit
    )

    fun updateList(
        onSuccess: (List<RestaurantItem>) -> Unit,
        onError: () -> Unit
    )
}