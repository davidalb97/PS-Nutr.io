package pt.ipl.isel.leic.ps.androidclient.ui.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class LiveDataHandler<M> {

    private val mediatorLiveData: MediatorLiveData<List<M>> = MediatorLiveData()
    val mapped: List<M> get() = mediatorLiveData.value ?: emptyList()
    private val _monitor = Object()
    private val prevLiveData: LiveData<*>? = null

    private val sources = mutableListOf<LiveData<*>>()
    private fun <T> observeExternal(newLiveData: LiveData<List<T>>, mapper: (T) -> M, consumer: (List<M>) -> Unit) {
        synchronized(_monitor) {
            if(prevLiveData != null) {
                mediatorLiveData.removeSource(prevLiveData)
            }
            mediatorLiveData.addSource(newLiveData) { newList ->
                consumer(newList.map(mapper))
            }
        }
    }

    fun <T> set(newLiveData: LiveData<List<T>>, mapper: (T) -> M) {
        observeExternal(newLiveData, mapper) {
            set(it)
        }
    }

    fun set(newList: List<M>) {
        mediatorLiveData.value = newList
    }

    fun <T> add(newLiveData: LiveData<List<T>>, mapper: (T) -> M) {
        observeExternal(newLiveData, mapper) {
            add(it)
        }
    }

    fun add(newList: List<M>) {
        val currValues = mapped.toMutableList()
        currValues.addAll(newList)
        mediatorLiveData.value = currValues
    }

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observe(owner: LifecycleOwner, observer: (List<M>) -> Unit) {
        mediatorLiveData.observe(owner, Observer {
            observer(it)
        })
    }
}