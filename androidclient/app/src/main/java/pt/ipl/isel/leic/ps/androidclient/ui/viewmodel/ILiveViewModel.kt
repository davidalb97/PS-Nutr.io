package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

interface ILiveViewModel<T> {

    fun updateLiveListExchangingLiveData(
        onSuccess: (List<Restaurant>) -> Unit,
        onError: () -> Unit
    )

    fun updateLiveList(
        onSuccess: (List<Restaurant>) -> Unit,
        onError: () -> Unit
    ): LiveData<List<T>>?
}