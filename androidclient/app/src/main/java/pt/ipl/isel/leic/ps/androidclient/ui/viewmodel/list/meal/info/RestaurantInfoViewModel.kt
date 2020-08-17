package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.live.LiveDataHandler
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

class RestaurantInfoViewModel : MealItemListViewModel {

    private val restaurantInfoLiveDataHandler = LiveDataHandler<RestaurantInfo>()
    val restaurantInfo get() = restaurantInfoLiveDataHandler.value

    constructor(restaurantId: String) : super(
        navDestination = Navigation.SEND_TO_MEAL_DETAIL,
        actions = listOf(
            ItemAction.FAVORITE,
            ItemAction.CALCULATE,
            ItemAction.REPORT,
            ItemAction.EDIT,
            ItemAction.VOTE
        ),
        source = Source.API,
        restaurantId = restaurantId
    )

    constructor(parcel: Parcel) : super(parcel) {
        val restaurantInfo: RestaurantInfo? =
            parcel.readParcelable(RestaurantInfo::class.java.classLoader)
        restaurantInfoLiveDataHandler.restoreFromValue(restaurantInfo)
    }

    override fun update() {
        if (!restaurantInfoLiveDataHandler.tryRestore()) {
            restaurantRepository.getRestaurantInfoById(
                restaurantId!!,
                restaurantInfoLiveDataHandler::set,
                onError
            )
        }
    }

    fun observeInfo(owner: LifecycleOwner, observer: (RestaurantInfo) -> Unit) {
        restaurantInfoLiveDataHandler.observe(owner) { restaurantInfo ->
            onRestaurantInfo(restaurantInfo)
            observer(restaurantInfo)
        }
    }

    private fun onRestaurantInfo(restaurantInfo: RestaurantInfo) {
        super.restoreFromList(restaurantInfo.meals.plus(restaurantInfo.suggestedMeals))
        super.tryRestore()
    }

    fun report(reportMsg: String, onSuccess: () -> Unit, onError: (VolleyError) -> Unit) {
        restaurantRepository.report(
            restaurantId = restaurantId!!,
            reportMsg = reportMsg,
            onSuccess = onSuccess,
            onError = onError,
            userSession = requireUserSession()
        )
    }

    fun vote(vote: VoteState, onSuccess: () -> Unit, onError: (VolleyError) -> Unit) {
        restaurantRepository.putVote(
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
        restaurantRepository.putFavorite(
            restaurantId = restaurantId!!,
            isFavorite = !restaurantInfo.isFavorite,
            success = {
                restaurantInfo.isFavorite = !restaurantInfo.isFavorite
                onSuccess()
            },
            error = onError,
            userSession = requireUserSession()
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        restaurantInfoLiveDataHandler.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantInfoViewModel> {


        override fun createFromParcel(parcel: Parcel): RestaurantInfoViewModel =
            RestaurantInfoViewModel(
                parcel
            )

        override fun newArray(size: Int): Array<RestaurantInfoViewModel?> {
            return arrayOfNulls(size)
        }

    }
}