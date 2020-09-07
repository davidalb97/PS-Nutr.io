package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import kotlin.reflect.KClass

class RestaurantListViewModel : BaseListViewModel<RestaurantItem> {

    val navDestination: Navigation
    val actions: List<ItemAction>
    var latitude: Double? = null
    var longitude: Double? = null

    //TODO use cuisines search filter
    var cuisines: List<Cuisine>?

    constructor(
        navDestination: Navigation,
        actions: List<ItemAction>,
        cuisines: List<Cuisine>? = null
    ) {
        this.navDestination = navDestination
        this.actions = actions
        this.cuisines = cuisines
    }

    constructor(parcel: Parcel) : super(parcel) {
        navDestination = Navigation.values()[parcel.readInt()]
        actions = parcel.readListCompat(ItemAction::class)
        cuisines = parcel.readListCompat(Cuisine::class)
        latitude = parcel.readSerializable() as Double?
        longitude = parcel.readSerializable() as Double?
    }

    override fun fetch() {
        restaurantRepository.getNearbyRestaurants(
            latitude = latitude!!,
            longitude = longitude!!,
            count = count,
            skip = skip,
            cuisines = cuisines,
            userSession = getUserSession(),
            success = liveDataHandler::add,
            error = onError
        )
    }

    fun putFavorite(
        restaurantItem: RestaurantItem,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) = restaurantRepository.changeFavorite(
        restaurantId = requireNotNull(restaurantItem.id),
        isFavorite = !restaurantItem.favorites.isFavorite,
        success = {
            restaurantItem.favorites.isFavorite = !restaurantItem.favorites.isFavorite
            onSuccess()
        },
        error = onError,
        userSession = requireUserSession()
    )

    fun report(
        restaurantItem: RestaurantItem,
        reportMsg: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) = restaurantRepository.addReport(
        restaurantId = requireNotNull(restaurantItem.id),
        reportMsg = reportMsg,
        onSuccess = {
            restaurantItem.favorites.isFavorite = !restaurantItem.favorites.isFavorite
            onSuccess()
        },
        onError = onError,
        userSession = requireUserSession()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeInt(navDestination.ordinal)
        dest?.writeList(actions)
        dest?.writeList(cuisines ?: emptyList<Cuisine>())
        dest?.writeSerializable(latitude)
        dest?.writeSerializable(longitude)
    }

    override fun getModelClass(): KClass<RestaurantItem> = RestaurantItem::class

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantListViewModel> {
        override fun createFromParcel(parcel: Parcel): RestaurantListViewModel =
            TODO("Restore RestaurantRecyclerViewModel from bundle")


        override fun newArray(size: Int): Array<RestaurantListViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
