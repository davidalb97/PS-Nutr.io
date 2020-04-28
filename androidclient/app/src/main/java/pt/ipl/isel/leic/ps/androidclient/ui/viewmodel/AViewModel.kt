package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.*

//data class LiveInfo<T>(var list: MutableList<T>, var requestPending: Boolean = false)

abstract class AViewModel<T> : ViewModel() {

    var mediatorLiveData: MediatorLiveData<List<T>> = MediatorLiveData()
    var liveData: LiveData<List<T>>? = null
    //val liveData: MutableLiveData<MutableList<T>> = MutableLiveData()
    //private lateinit var currentQuery: Iterable<Pair<"TODO", String>>


    /**
     * Adds more items to the Recycler List
     */
    fun addItems(items: MutableList<T>) {
        mediatorLiveData.value = items
    }

    /**
     * Adds more items to the existing ones inside the Recycler List
     */
    fun addMoreExchangingLiveData(items: MutableList<T>) {
        if (liveData != null)
            mediatorLiveData.removeSource(liveData!!)
        liveData = fetchLiveData()
        mediatorLiveData.addSource(liveData!!) {
            mediatorLiveData.value = it
        }
    }

    abstract fun fetchLiveData(): LiveData<List<T>>?

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observe(owner: LifecycleOwner, observer: (List<T>) -> Unit) {
        mediatorLiveData.observe(owner, Observer {
            observer(it)
        })
    }

    abstract fun getItems()

    abstract fun getMoreItems()

    abstract fun deleteItem(item: T)
}