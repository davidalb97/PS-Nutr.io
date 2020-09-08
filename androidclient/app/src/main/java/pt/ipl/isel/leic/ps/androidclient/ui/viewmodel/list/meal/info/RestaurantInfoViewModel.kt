package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.live.LiveDataHandler
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

class RestaurantInfoViewModel : MealItemListViewModel {

    //TODO override removeObservers
    private val restaurantInfoLiveDataHandler = LiveDataHandler<RestaurantInfo>()
    val restaurantInfo get() = restaurantInfoLiveDataHandler.value
    var addedMeal: MealItem? = null

    //Parameterless constructor due to byNavGraphsViewModel
    constructor() : super(
        navDestination = Navigation.SEND_TO_MEAL_DETAIL,
        actions = listOf(
            ItemAction.FAVORITE,
            ItemAction.CALCULATE,
            ItemAction.REPORT,
            ItemAction.VOTE
        ),
        source = Source.API
    )

    constructor(parcel: Parcel) : super(parcel) {
        val restaurantInfo: RestaurantInfo? =
            parcel.readParcelable(RestaurantInfo::class.java.classLoader)
        restaurantInfoLiveDataHandler.restoreFromValue(restaurantInfo)
        addedMeal = parcel.readParcelable(MealItem::class.java.classLoader)
    }

    override fun setupList() {
        if (restaurantInfo != null) {
            restaurantInfoLiveDataHandler.notifyChanged()
        } else triggerFetch()
    }

    override fun tryRestore(): Boolean {
        return restaurantInfoLiveDataHandler.tryRestore()
    }

    override fun fetch() {
        restaurantRepository.getRestaurantInfoById(
            restaurantId = restaurantId!!,
            userSession = getUserSession(),
            success = restaurantInfoLiveDataHandler::set,
            error = onError
        )
    }

    fun observeInfo(owner: LifecycleOwner, observer: (RestaurantInfo) -> Unit) {
        restaurantInfoLiveDataHandler.observe(owner) { restaurantInfo ->
            onRestaurantInfo(restaurantInfo)
            observer(restaurantInfo)
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        restaurantInfoLiveDataHandler.removeObservers(owner)
        super.removeObservers(owner)
    }

    private fun onRestaurantInfo(restaurantInfo: RestaurantInfo) {
        super.restoreFromList(restaurantInfo.meals.plus(restaurantInfo.suggestedMeals))
        super.tryRestore()
    }

    fun report(reportMsg: String, onSuccess: () -> Unit, onError: (VolleyError) -> Unit) {
        restaurantRepository.addReport(
            restaurantId = restaurantId!!,
            reportMsg = reportMsg,
            onSuccess = onSuccess,
            onError = onError,
            userSession = requireUserSession()
        )
    }

    fun vote(vote: VoteState, onSuccess: () -> Unit, onError: (VolleyError) -> Unit) {
        restaurantRepository.changeVote(
            id = restaurantId!!,
            vote = vote,
            success = {
                restaurantInfo?.votes?.userHasVoted = vote
                onSuccess()
            },
            onError = onError,
            userSession = requireUserSession()
        )
    }

    fun favorite(onSuccess: () -> Unit, onError: (VolleyError) -> Unit) {
        val restaurantInfo = restaurantInfo!!
        restaurantRepository.changeFavorite(
            restaurantId = restaurantId!!,
            isFavorite = !restaurantInfo.favorites.isFavorite,
            success = {
                restaurantInfo.favorites.isFavorite = !restaurantInfo.favorites.isFavorite
                onSuccess()
            },
            error = onError,
            userSession = requireUserSession()
        )
    }

    fun addRestaurantMeal(
        mealItem: MealItem,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        NutrioApp.mealRepository.addRestaurantMeal(
            mealItem = mealItem,
            restaurantItem = restaurantInfo!!,
            onSuccess = onSuccess,
            onError = onError,
            userSession = requireUserSession()
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        restaurantInfoLiveDataHandler.writeToParcel(dest, flags)
        dest?.writeParcelable(addedMeal, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantInfoViewModel> {

        override fun createFromParcel(parcel: Parcel): RestaurantInfoViewModel =
            RestaurantInfoViewModel(parcel)

        override fun newArray(size: Int): Array<RestaurantInfoViewModel?> {
            return arrayOfNulls(size)
        }

    }
}