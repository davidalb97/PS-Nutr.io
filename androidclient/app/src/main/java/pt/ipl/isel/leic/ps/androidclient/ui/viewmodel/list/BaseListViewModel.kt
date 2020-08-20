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
abstract class BaseListViewModel<T : Parcelable>() : ViewModel(), Parcelable {

    constructor(parcel: Parcel) : this() {
        this.restoreFromParcel(parcel)
    }

    val log: Logger by lazy { Logger(javaClass) }
    val liveDataHandler = LiveDataListHandler<T>()
    val items: List<T> get() = liveDataHandler.mapped
    var skip = 0
    var count = 0
    var onError: (Throwable) -> Unit = log::e

    abstract fun update()

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
     * Attempts to restore all the previously saved items on [liveDataHandler] from [parcel].
     * If the [parcel] contained the [LiveDataListHandler] object,
     * the next call to [tryRestore] will succeed.
     * @param parcel The [Parcel] to read the [LiveDataListHandler] from.
     */
    @CallSuper
    open fun restoreFromParcel(parcel: Parcel) {
        liveDataHandler.restoreFromParcel(parcel, getModelClass())
    }

    /**
     * Restores the [liveDataHandler].
     * This operation will only succeed if a previous call from [restoreFromList] or
     * [restoreFromParcel] was made.
     * @return If the restored operation was successful.
     */
    open fun tryRestore(): Boolean = liveDataHandler.tryRestore()

    abstract fun getModelClass(): KClass<T>
}