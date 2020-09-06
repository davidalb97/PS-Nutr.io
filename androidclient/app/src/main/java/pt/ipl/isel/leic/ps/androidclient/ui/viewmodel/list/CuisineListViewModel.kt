package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.cuisineRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import kotlin.reflect.KClass

//TODO Delete CuisineListViewModel if not being used
open class CuisineListViewModel(
    val navDestination: Navigation,
    val actions: List<ItemAction>
) : BaseListViewModel<Cuisine>() {

    constructor(parcel: Parcel) : this(
        navDestination = Navigation.values()[parcel.readInt()],
        actions = parcel.readListCompat(ItemAction::class)
    ) {
        super.restoreFromParcel(parcel)
    }

    override fun fetch() {
        if (!super.tryRestore()) {
            cuisineRepository.getCuisines(count, skip, liveDataHandler::set, onError)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(navDestination.ordinal)
        dest?.writeList(actions)
        super.writeToParcel(dest, flags)
    }

    override fun getModelClass(): KClass<Cuisine> = Cuisine::class

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CuisineListViewModel> {

        override fun createFromParcel(parcel: Parcel): CuisineListViewModel {
            return CuisineListViewModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<CuisineListViewModel?> {
            return arrayOfNulls(size)
        }

    }

}