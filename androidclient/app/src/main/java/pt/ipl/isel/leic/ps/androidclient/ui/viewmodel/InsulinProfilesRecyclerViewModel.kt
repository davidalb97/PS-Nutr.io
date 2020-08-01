package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone

open class InsulinProfilesRecyclerViewModel() : ARecyclerViewModel<InsulinProfile>() {

    var jwt: String? = null
    var refreshLayout: SwipeRefreshLayout? = null

    constructor(parcel: Parcel) : this()

    fun addDbInsulinProfile(profile: InsulinProfile) =
        insulinProfilesRepository.addProfile(profile, jwt, onError)

    fun deleteItem(profileName: String) =
        insulinProfilesRepository.deleteProfile(profileName, jwt, onError)

    override fun update() {
        this.liveDataHandler.set(insulinProfilesRepository.getAllProfiles()) {
            insulinProfilesRepository.insulinProfileMapper.mapToModel(it)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<InsulinProfilesRecyclerViewModel> {
        override fun createFromParcel(parcel: Parcel): InsulinProfilesRecyclerViewModel {
            return InsulinProfilesRecyclerViewModel(parcel)
        }

        override fun newArray(size: Int): Array<InsulinProfilesRecyclerViewModel?> {
            return arrayOfNulls(size)
        }
    }
}