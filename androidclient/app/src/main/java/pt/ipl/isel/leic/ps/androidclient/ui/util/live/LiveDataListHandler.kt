package pt.ipl.isel.leic.ps.androidclient.ui.util.live

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilter
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import kotlin.reflect.KClass

class LiveDataListHandler<M : Parcelable> {

    private val log = Logger(LiveDataListHandler::class)
    private val _monitor = Object()
    private val mediatorLiveData: MediatorLiveData<List<M>> = MediatorLiveData()
    private val prevLiveData: LiveData<*>? = null
    private var restoredItems: List<M>? = null
    val mapped: List<M> get() = mediatorLiveData.value ?: emptyList()
    var filter: IItemListFilter<M>? = null

    /**
     * Observes a [LiveData]<[T]> and passes the observed values to the [consumer].
     * All values are mapped with the [mapper] function.
     * @param newLiveData The [LiveData]<[T]> containing the values to be observed.
     * @param mapper The mapper function to transform the observed values from [T] to [M].
     * @param consumer The consumer to accept the transformed values.
     */
    private fun <T> observeExternal(
        newLiveData: LiveData<List<T>>,
        mapper: (T) -> M,
        consumer: (List<M>) -> Unit
    ) {
        synchronized(_monitor) {
            removeLastSource()
            mediatorLiveData.addSource(newLiveData) { newList ->
                consumer(newList.map(mapper))
            }
        }
    }

    /**
     * Sets the list of items in [LiveData]<[T]> to the [MediatorLiveData.setValue].
     * The items might be transformed with the [mapper] to match the type [M] when values are set.
     * @param newLiveData The [LiveData]<[T]> containing the new values to be set.
     * @param mapper The mapper function to transform the received values from [T] to [M].
     */
    fun <T> set(newLiveData: LiveData<List<T>>, mapper: (T) -> M) {
        observeExternal(newLiveData, mapper) {
            set(it)
        }
    }

    /**
     * Sets a list of items to current [MediatorLiveData.setValue].
     * @param newList The list of items to set.
     */
    fun set(newList: List<M>) {
        log.v("Setting ${newList.size} items")
        mediatorLiveData.value = filter?.let { filter ->
            newList.filter(filter::filter)
        } ?: newList
    }

    /**
     * Adds a [LiveData]<[T]> list of items to current [MediatorLiveData.setValue],
     * preserving the old values.
     * The items might be transformed with the [mapper] to match the type [M] when values are set.
     * @param newLiveData The [LiveData]<[T]> containing the new values to be added.
     * @param mapper The mapper function to transform the received values from [T] to [M].
     */
    fun <T> add(newLiveData: LiveData<List<T>>, mapper: (T) -> M) {
        observeExternal(newLiveData, mapper) {
            add(it)
        }
    }

    /**
     * Adds a list of items to current [mediatorLiveData].
     * @param newList The list of items to add.
     */
    fun add(newList: List<M>) {
        log.v("Adding ${newList.size} items")
        set(mapped.plus(newList))
    }

    /**
     * Add an [item] to the list
     * @param item The item to add.
     */
    fun add(item: M) {
        log.v("Adding 1 item")
        set(mapped.plus(item))
    }

    /**
     * Removes an [item] from the list if not null.
     * @param item The item to remove.
     */
    fun remove(item: M) {
        val currentVal = mediatorLiveData.value
        if (currentVal != null) {
            log.v("Removing 1 item")
            set(currentVal.minus(item))
        }
    }

    /**
     * Observes the [LiveData], given a [LifecycleOwner] and a [MutableList].
     * @param owner The [LifecycleOwner] that will observe the [mediatorLiveData].
     * @param observer The callback to receive [MediatorLiveData.setValue] changes.
     */
    fun observe(owner: LifecycleOwner, observer: (List<M>) -> Unit) {
        mediatorLiveData.observe(owner, {
            observer(it)
        })
    }

    /**
     * Removes the previous [LiveData] source from [mediatorLiveData] store on [prevLiveData].
     * This is useful do discard changes before they happen.
     */
    fun removeLastSource() {
        if (prevLiveData != null) {
            mediatorLiveData.removeSource(prevLiveData)
        }
    }

    /**
     * Removes all observers from [owner].
     * @param owner The [LifecycleOwner] that was observers.
     */
    fun removeObservers(owner: LifecycleOwner) {
        mediatorLiveData.removeObservers(owner)
    }

    /**
     * Attempts to restore all the previously saved items on [LiveData.getValue] list &
     * [restoredItems] list from [items] list into [restoredItems].
     * If the resulting [restoredItems] is not null, the next call to [tryRestore] will succeed.
     * @param items The items to restore.
     */
    fun restoreFromValues(items: List<M>?) {
        this.restoredItems = items
    }

    /**
     * Attempts to restore all the previously saved items on [LiveData.getValue] list &
     * [restoredItems] list from [src] parcel into [restoredItems].
     * If the resulting [restoredItems] is not null, the next call to [tryRestore] will succeed.
     * @param src The [Parcel] to read the lists from.
     * @param kClass the [KClass] of the generic type [M].
     */
    @Suppress("UNCHECKED_CAST")
    fun restoreFromParcel(src: Parcel, kClass: KClass<M>) {
        restoredItems = src.readArrayList(kClass.java.classLoader) as List<M>?
    }

    /**
     * Writes the current [LiveData.getValue] list & [restoredItems] list to [dest] parcel.
     * If all of the lists are null, the resulting list value stored will be null as well.
     * @param dest The parcel to write the lists into.
     */
    fun writeToParcel(dest: Parcel?) {
        dest?.writeList(
            mediatorLiveData.value
                ?.plus(restoredItems ?: emptyList())
                ?: restoredItems
        )
    }

    /**
     * Restores current values from [restoredItems] into [set].
     * This operation will only succeed if a previous call from [restoreFromValues] or
     * [restoreFromParcel] was made.
     * @return If the restored operation was successful.
     */
    fun tryRestore(): Boolean {
        val restoredItems = this.restoredItems
        if (restoredItems != null) {
            set(restoredItems)
            this.restoredItems = null
            return true
        }
        return false
    }

    fun notifyChanged() {
        mediatorLiveData.value = mediatorLiveData.value
    }
}


