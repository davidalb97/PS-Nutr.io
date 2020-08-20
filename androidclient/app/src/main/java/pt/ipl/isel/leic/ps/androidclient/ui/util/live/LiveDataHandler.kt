package pt.ipl.isel.leic.ps.androidclient.ui.util.live

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import kotlin.reflect.KClass

class LiveDataHandler<M : Parcelable> {

    private val _monitor = Object()
    private val mediatorLiveData: MediatorLiveData<M> = MediatorLiveData()
    private val prevLiveData: LiveData<*>? = null
    private var restoredItem: M? = null
    val value get() = mediatorLiveData.value

    /**
     * Observes a [LiveData]<[T]> and passes the observed values to the [consumer].
     * All values are mapped with the [mapper] function.
     * @param newLiveData The [LiveData]<[T]> containing the values to be observed.
     * @param mapper The mapper function to transform the observed values from [T] to [M].
     * @param consumer The consumer to accept the transformed values.
     */
    private fun <T> observeExternal(
        newLiveData: LiveData<T>,
        mapper: (T) -> M,
        consumer: (M) -> Unit
    ) {
        synchronized(_monitor) {
            removeLastSource()
            mediatorLiveData.addSource(newLiveData) { item ->
                consumer(mapper(item))
            }
        }
    }

    /**
     * Sets the value in [LiveData]<[T]> to the [MediatorLiveData.setValue].
     * The item might be transformed with the [mapper] to match the type [M] when the value is set.
     * @param newLiveData The [LiveData]<[T]> containing the new value to be set.
     * @param mapper The mapper function to transform the received value from [T] to [M].
     */
    fun <T> set(newLiveData: LiveData<T>, mapper: (T) -> M) {
        observeExternal(newLiveData, mapper) {
            set(it)
        }
    }

    /**
     * Sets a value of items to current [MediatorLiveData.setValue].
     * @param newValue The value to set.
     */
    fun set(newValue: M) {
        mediatorLiveData.value = newValue
    }

    /**
     * Observes the [LiveData], given a [LifecycleOwner] and a value [M].
     * @param owner The [LifecycleOwner] that will observe the [mediatorLiveData].
     * @param observer The callback to receive [MediatorLiveData.setValue] changes.
     */
    fun observe(owner: LifecycleOwner, observer: (M) -> Unit) {
        mediatorLiveData.observe(owner, Observer {
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
     * Attempts to restore the previously saved value on [LiveData.getValue] &
     * [restoredItem] from [item] into [restoredItem].
     * If the resulting [restoredItem] is not null, the next call to [tryRestore] will succeed.
     * @param item The item to restore.
     */
    fun restoreFromValue(item: M?) {
        this.restoredItem = item
    }

    /**
     * Attempts to restore the previously saved value on [LiveData.getValue] &
     * [restoredItem] from [src] parcel into [restoredItem].
     * If the resulting [restoredItem] is not null, the next call to [tryRestore] will succeed.
     * @param src The [Parcel] to read the value from.
     * @param kClass the [KClass] of the generic type [M].
     */
    fun restoreFromParcel(src: Parcel, kClass: KClass<M>) {
        restoredItem = src.readParcelable(kClass.java.classLoader)
    }

    /**
     * Writes the current [LiveData.getValue] list & [restoredItem] to [dest] parcel.
     * If the value is null, the resulting value stored will be null as well.
     * @param dest The parcel to write the value into.
     */
    fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(mediatorLiveData.value ?: restoredItem, flags)
    }

    /**
     * Restores current value from [restoredItem] into [set].
     * This operation will only succeed if a previous call from [restoreFromValue] or
     * [restoreFromParcel] was made.
     * @return If the restored operation was successful.
     */
    fun tryRestore(): Boolean {
        val restoredItem = this.restoredItem
        if (restoredItem != null) {
            set(restoredItem)
            this.restoredItem = null
            return true
        }
        return false
    }
}


