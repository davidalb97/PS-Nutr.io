package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile

class InsulinProfilesRecyclerViewModel() : ARecyclerViewModel<InsulinProfile>() {

    constructor(parcel: Parcel) : this()

    fun addInsulinProfile(profile: InsulinProfile) =
        insulinProfilesRepository.addProfile(profile)

    fun deleteItem(profile: InsulinProfile) =
        insulinProfilesRepository.deleteProfile(profile)

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