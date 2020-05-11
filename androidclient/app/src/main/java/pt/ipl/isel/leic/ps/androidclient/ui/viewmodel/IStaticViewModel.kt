package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import pt.ipl.isel.leic.ps.androidclient.data.source.model.restaurant.Restaurant

interface IStaticViewModel<T> {

    fun updateListExchangingLiveData(
        onSuccess: (List<Restaurant>) -> Unit,
        onError: () -> Unit
    )

    fun updateList(
        onSuccess: (List<Restaurant>) -> Unit,
        onError: () -> Unit
    )
}