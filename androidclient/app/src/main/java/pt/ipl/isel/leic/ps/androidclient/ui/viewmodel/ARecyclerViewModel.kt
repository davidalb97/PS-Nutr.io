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

    /**
     * The parameters contains the pairs for the uri path and/or the query string
     */
    var parameters: HashMap<String, HashMap<String, String>> = HashMap()
    var skip = 0

    /**
     * Sets a item list to a Recycler List
     */
    fun setList(items: List<T>) {
        mediatorLiveData.value = items
    }

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observe(owner: LifecycleOwner, observer: (List<T>) -> Unit) {
        mediatorLiveData.observe(owner, Observer {
            observer(it)
        })
    }

    abstract fun fetchLiveData(): LiveData<List<T>>
}