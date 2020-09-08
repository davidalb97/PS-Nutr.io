package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
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

    fun addDbInsulinProfile(
        insulinProfile: InsulinProfile,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit
    ) = insulinProfilesRepository.addProfile(
        profileDb = insulinProfile,
        userSession = getUserSession(),
        onSuccess = onSuccess,
        onError = onError
    )

    fun deleteItem(
        insulinProfile: InsulinProfile,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit
    ) = insulinProfilesRepository.deleteProfile(
        insulinProfile = insulinProfile,
        userSession = getUserSession(),
        onSuccess = onSuccess,
        onError = onError
    )

    override fun fetch() {
        val userSession = getUserSession()
        if(userSession != null) {
            insulinProfilesRepository.getRemoteProfiles(
                userSession,
                onError = onError,
                onSuccess = liveDataHandler::set
            )
        }
        else {
            this.liveDataHandler.set(insulinProfilesRepository.getAllDbProfiles()) {
                insulinProfilesRepository.insulinProfileMapper.mapToModel(it)
            }
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