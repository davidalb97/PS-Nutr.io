package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import pt.ipl.isel.leic.ps.androidclient.ui.util.live.LiveDataListHandler
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel

abstract class BaseItemPickerViewModel<M : Parcelable> : BaseListViewModel<M> {

    val pickedLiveDataHandler = LiveDataListHandler<M>()
    val pickedItems: List<M> get() = pickedLiveDataHandler.mapped

    constructor() : super()

    constructor(parcel: Parcel): super(parcel) {
        pickedLiveDataHandler.restoreFromParcel(parcel, this.getModelClass())
    }

    override fun tryRestore(): Boolean {
        var tryRestore = liveDataHandler.tryRestore()
        tryRestore = tryRestore || pickedLiveDataHandler.tryRestore()
        return tryRestore
    }

    /**
     * Observes the LiveData, given a LifeCycleOwner and a MutableList
     */
    fun observePicked(owner: LifecycleOwner, observer: (List<M>) -> Unit) {
        pickedLiveDataHandler.observe(owner, observer)
    }

    fun observeRemaining(owner: LifecycleOwner, observer: (List<M>) -> Unit) {
        liveDataHandler.observe(owner, observer)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        pickedLiveDataHandler.removeObservers(owner)
        super.removeObservers(owner)
    }

    fun pick(picked: M) {
        pickedLiveDataHandler.add(picked)
        liveDataHandler.remove(picked)
    }

    fun unPick(unPicked: M) {
        pickedLiveDataHandler.remove(unPicked)
        liveDataHandler.add(unPicked)
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeList(pickedLiveDataHandler.mapped)
        super.writeToParcel(dest, flags)
    }

    override fun restoreFromParcel(parcel: Parcel) {
        pickedLiveDataHandler.restoreFromParcel(parcel, getModelClass())
        super.restoreFromParcel(parcel)
    }
}