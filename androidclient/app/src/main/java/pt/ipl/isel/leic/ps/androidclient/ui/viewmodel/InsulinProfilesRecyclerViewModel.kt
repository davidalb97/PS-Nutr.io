package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile

open class InsulinProfilesRecyclerViewModel() : ARecyclerViewModel<InsulinProfile>() {

    lateinit var jwt: String
    lateinit var refreshLayout: SwipeRefreshLayout

    constructor(parcel: Parcel) : this()

    fun addDbInsulinProfile(profile: InsulinProfile) =
        insulinProfilesRepository.addProfile(profile, jwt, onError)

    fun deleteItem(profileName: String) =
        insulinProfilesRepository.deleteProfile(profileName, jwt, onError)

    override fun update() {

        this.liveDataHandler.set(insulinProfilesRepository.getAllDbProfiles()) {
            insulinProfilesRepository.insulinProfileMapper.mapToModel(it)
        }

        insulinProfilesRepository.getAllProfiles(
            jwt,
            this.liveDataHandler::add,
            onError
        )

        if (refreshLayout.isRefreshing) {
            refreshLayout.isRefreshing = false
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