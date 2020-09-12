package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import pt.ipl.isel.leic.ps.androidclient.ui.util.live.LiveDataListHandler
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel
import kotlin.reflect.KClass

abstract class BaseItemPickerViewModel<I : Parcelable> : BaseListViewModel<I> {

    val pickedLiveDataHandler = LiveDataListHandler<I>()
    val pickedItems: List<I> get() = pickedLiveDataHandler.mapped

    constructor(itemClass: KClass<I>) : super(itemClass)

    constructor(parcel: Parcel, itemClass: KClass<I>): super(parcel, itemClass) {
        pickedLiveDataHandler.restoreFromParcel(parcel, itemClass)
    }

    override fun tryRestore(): Boolean {
        val tryRestoreUnpicked = liveDataHandler.tryRestore()
        val tryRestorePicked = pickedLiveDataHandler.tryRestore()
        return tryRestoreUnpicked || tryRestorePicked
    }

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observePicked(owner: LifecycleOwner, observer: (List<I>) -> Unit) {
        pickedLiveDataHandler.observe(owner, observer)
    }

    fun observeRemaining(owner: LifecycleOwner, observer: (List<I>) -> Unit) {
        liveDataHandler.observe(owner, observer)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        pickedLiveDataHandler.removeObservers(owner)
        super.removeObservers(owner)
    }

    fun pick(picked: I) {
        pickedLiveDataHandler.add(picked)
        liveDataHandler.remove(picked)
    }

    fun unPick(unPicked: I) {
        pickedLiveDataHandler.remove(unPicked)
        liveDataHandler.add(unPicked)
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeList(pickedLiveDataHandler.mapped)
    }
}