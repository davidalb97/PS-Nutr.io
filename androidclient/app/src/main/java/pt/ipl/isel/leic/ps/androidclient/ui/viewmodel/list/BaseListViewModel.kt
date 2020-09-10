package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.live.LiveDataListHandler
import kotlin.reflect.KClass

/**
 * A generic View Model for [RecyclerView] lists, using a [LiveDataListHandler] to handle the list.
 */

const val DEFAULT_COUNT = 10
const val DEFAULT_SKIP = 0

abstract class BaseListViewModel<T : Parcelable> : ViewModel, Parcelable {

    val itemClass: KClass<T>
    val log: Logger by lazy { Logger(javaClass) }
    val liveDataHandler = LiveDataListHandler<T>()
    val items: List<T> get() = liveDataHandler.mapped
    var pendingRequest: Boolean = false
    var skip: Int? = DEFAULT_SKIP
    var count: Int? = DEFAULT_COUNT
    var onError: (Throwable) -> Unit = log::e

    constructor(itemClass: KClass<T>): super() {
        this.itemClass = itemClass
    }

    constructor(parcel: Parcel, itemClass: KClass<T>) : this(itemClass) {
        liveDataHandler.restoreFromParcel(parcel, itemClass)
    }

    open fun setupList() {
        if(items.isNotEmpty()) {
            liveDataHandler.notifyChanged()
        } else triggerFetch()
    }

    fun triggerFetch() {
        pendingRequest = true
        if (!tryRestore()) {
            fetch()
        }
    }

    protected abstract fun fetch()

    /**
     * Observes the [LiveDataListHandler], given a [LifecycleOwner] and a [MutableList].
     */
    open fun observe(owner: LifecycleOwner, observer: (List<T>) -> Unit) {
        liveDataHandler.observe(owner, observer)
    }

    /**
     * Saves [LiveDataListHandler] state to a [Parcel].
     * @param dest The [Parcel] to store the state.
     */
    @CallSuper
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        liveDataHandler.writeToParcel(dest)
    }

    /**
     * Attempts to restore all the previously saved items on [liveDataHandler] from [restoredItems].
     * If [restoredItems] was not null, the next call to [tryRestore] will succeed.
     * @param restoredItems The [List]<[T]> to store the state.
     */
    fun restoreFromList(restoredItems: List<T>?) {
        liveDataHandler.restoreFromValues(restoredItems)
    }

    /**
     * Restores the [liveDataHandler].
     * This operation will only succeed if the ViewModel was constructed from a [Parcel]
     * or a previous call to [restoreFromList] was made.
     * @return If the restored operation was successful.
     */
    open fun tryRestore(): Boolean = liveDataHandler.tryRestore()

    open fun removeObservers(owner: LifecycleOwner) {
        liveDataHandler.removeObservers(owner)
    }
}