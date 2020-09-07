package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import kotlin.reflect.KClass

open class InsulinProfilesListViewModel : BaseListViewModel<InsulinProfile> {

    val actions: List<ItemAction>

    constructor(actions: List<ItemAction>) {
        this.actions = actions
    }

    constructor(parcel: Parcel) : super(parcel) {
        actions = parcel.readListCompat(ItemAction::class)
    }

    fun addDbInsulinProfile(profile: InsulinProfile, userSession: UserSession?) =
        insulinProfilesRepository.addProfile(profile, userSession, onError)

    fun deleteItem(profileName: String, userSession: UserSession?) =
        insulinProfilesRepository.deleteProfile(profileName, userSession, onError)

    override fun fetch() {
        this.liveDataHandler.set(insulinProfilesRepository.getAllProfiles()) {
            insulinProfilesRepository.insulinProfileMapper.mapToModel(it)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeList(actions)
    }

    override fun getModelClass(): KClass<InsulinProfile> = InsulinProfile::class

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<InsulinProfilesListViewModel> {
        override fun createFromParcel(parcel: Parcel): InsulinProfilesListViewModel {
            return InsulinProfilesListViewModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<InsulinProfilesListViewModel?> {
            return arrayOfNulls(size)
        }
    }
}