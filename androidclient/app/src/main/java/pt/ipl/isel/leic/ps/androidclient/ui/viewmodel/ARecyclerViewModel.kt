package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.*

//data class LiveInfo<T>(var list: MutableList<T>, var requestPending: Boolean = false)

/**
 * A generic View Model for Recycler Lists
 */
abstract class ARecyclerViewModel<T> : ViewModel() {

    var mediatorLiveData: MediatorLiveData<List<T>> = MediatorLiveData()
    var liveData: LiveData<List<T>>? = null
    val items: List<T> get() = mediatorLiveData.value ?: emptyList()
    //private lateinit var currentQuery: Iterable<Pair<"TODO", String>>

    /**
     * Adds more items to the Recycler List
     */
    fun getItems(items: MutableList<T>) {
        mediatorLiveData.value = items
    }

    /**
     * Adds more items to the existing ones inside the Recycler List
     */
    fun getMoreItemsExchangingLiveData(items: MutableList<T>) {
        if (liveData != null)
            mediatorLiveData.removeSource(liveData!!)
        liveData = fetchLiveData()
        mediatorLiveData.addSource(liveData!!) {
            mediatorLiveData.value = it
        }
    }

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observe(owner: LifecycleOwner, observer: (List<T>) -> Unit) {
        mediatorLiveData.observe(owner, Observer {
            observer(it)
        })
    }

    abstract fun fetchLiveData(): LiveData<List<T>>?

    //abstract fun deleteItem(item: T)
}