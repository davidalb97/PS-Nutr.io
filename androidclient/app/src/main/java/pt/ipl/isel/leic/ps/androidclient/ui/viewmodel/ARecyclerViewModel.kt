package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.lifecycle.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.LiveDataHandler

/**
 * A generic View Model for Recycler Lists
 */
abstract class ARecyclerViewModel<T> : ViewModel(), Parcelable {

    /**
     * The parameters contains the pairs for the uri path and/or the query string
     */
    //var parameters: HashMap<String, String> = hashMapOf()
    var skip = 0
    var count = 0

    lateinit var onError: (Throwable) -> Unit

    protected val liveDataHandler = LiveDataHandler<T>()
    val items: List<T> get() = liveDataHandler.mapped

    abstract fun update()

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observe(owner: LifecycleOwner, observer: (List<T>) -> Unit) {
        liveDataHandler.observe(owner, observer)
    }

}