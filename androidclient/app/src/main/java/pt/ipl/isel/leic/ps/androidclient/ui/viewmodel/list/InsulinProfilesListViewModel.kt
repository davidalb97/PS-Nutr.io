package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat

private val ITEM_CLASS = InsulinProfile::class

class InsulinProfilesListViewModel : BaseListViewModel<InsulinProfile> {

    var argumentMeal: MealItem?
    val actions: List<ItemAction>

    constructor(
        argumentMeal: MealItem? = null,
        actions: List<ItemAction>
    ) : super(ITEM_CLASS) {
        this.argumentMeal = argumentMeal
        this.actions = actions
    }

    constructor(parcel: Parcel) : super(parcel, ITEM_CLASS) {
        argumentMeal = parcel.readParcelable(MealItem::class.java.classLoader)
        actions = parcel.readListCompat(ItemAction::class)
    }

    fun addDbInsulinProfile(
        insulinProfile: InsulinProfile,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit
    ) = insulinProfilesRepository.addProfile(
        profileDb = insulinProfile,
        userSession = getUserSession(),
        onSuccess = {
            liveDataHandler.add(insulinProfile)
            onSuccess()
        },
        onError = onError
    )

    fun deleteItem(
        insulinProfile: InsulinProfile,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit
    ) {
        insulinProfilesRepository.deleteProfile(
            insulinProfile = insulinProfile,
            userSession = getUserSession(),
            onError = onError,
            onSuccess = {
                liveDataHandler.remove(insulinProfile)
                onSuccess()
            }
        )
    }

    override fun fetch() {
        val userSession = getUserSession()
        if (userSession != null) {
            insulinProfilesRepository.getRemoteProfiles(
                userSession,
                onError = onError,
                onSuccess = liveDataHandler::set
            )
        } else {
            this.liveDataHandler.set(insulinProfilesRepository.getAllDbProfiles()) {
                insulinProfilesRepository.insulinProfileMapper.mapToModel(it)
            }
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeParcelable(argumentMeal, flags)
        dest?.writeList(actions)
    }

    companion object CREATOR : Parcelable.Creator<InsulinProfilesListViewModel> {
        override fun createFromParcel(parcel: Parcel) = InsulinProfilesListViewModel(parcel)

        override fun newArray(size: Int): Array<InsulinProfilesListViewModel?> {
            return arrayOfNulls(size)
        }
    }
}