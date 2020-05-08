package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcelable
import androidx.lifecycle.*

/**
 * A generic View Model for Recycler Lists
 */
abstract class ARecyclerViewModel<T> : ViewModel(), Parcelable {

    var mediatorLiveData: MediatorLiveData<List<T>> = MediatorLiveData()
    var liveData: LiveData<List<T>>? = null
    val items: List<T> get() = mediatorLiveData.value ?: emptyList()
    var preList: ArrayList<T> = ArrayList()

    lateinit var onSuccess: (List<T>) -> Unit
    lateinit var onError: (Exception) -> Unit

    /**
     * The parameters contains the pairs for the uri path and/or the query string
     */
    var parameters: HashMap<String, HashMap<String, String>> = hashMapOf(
        Pair("path", HashMap()),
        Pair("query", HashMap())
    )
    var skip = 0

    /**
     * Sets/updates a Recycler List with a passed item list
     */
    fun updateList(items: List<T>) {
        preList.addAll(items)
        mediatorLiveData.value = preList
    }

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observe(owner: LifecycleOwner, observer: (List<T>) -> Unit) {
        mediatorLiveData.observe(owner, Observer {
            observer(it)
        })
    }

    /**
     * Adds more items to the existing ones inside the Recycler List
     */
    fun updateListFromLiveData() {
        if (liveData != null)
            mediatorLiveData.removeSource(liveData!!)
        liveData = fetchLiveData()
        mediatorLiveData.addSource(liveData!!) {
            mediatorLiveData.value = it
        }
    }

    abstract fun fetchLiveData(): LiveData<List<T>>
}