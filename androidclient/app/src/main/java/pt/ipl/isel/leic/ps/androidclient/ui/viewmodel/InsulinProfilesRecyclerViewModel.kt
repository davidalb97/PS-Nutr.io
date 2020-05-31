package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbInsulinProfileDto

class InsulinProfilesRecyclerViewModel() : ARecyclerViewModel<DbInsulinProfileDto>() {

    constructor(parcel: Parcel) : this() {
    }

    fun addInsulinProfile(profileDb: DbInsulinProfileDto) =
        insulinProfilesRepository.addProfile(profileDb)

    fun deleteItem(profileDb: DbInsulinProfileDto) =
        insulinProfilesRepository.deleteProfile(profileDb)

    override fun fetchLiveData(): LiveData<List<DbInsulinProfileDto>> =
        insulinProfilesRepository.getAllProfiles()

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