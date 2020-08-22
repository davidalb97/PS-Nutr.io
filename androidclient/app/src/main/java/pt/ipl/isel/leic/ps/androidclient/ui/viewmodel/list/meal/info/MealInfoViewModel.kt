package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.live.LiveDataHandler
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.RestaurantListViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealInfoListViewModel

open class MealInfoViewModel : MealInfoListViewModel {

    private val mealInfoLiveDataHandler = LiveDataHandler<MealInfo>()
    val mealInfo get() = mealInfoLiveDataHandler.value
    val mealItem: MealItem?

    constructor(
        mealInfo: MealInfo,
        ingredientActions: List<ItemAction>
    ) : super(
        source = mealInfo.source,
        restaurantId = mealInfo.restaurantSubmissionId,
        cuisines = mealInfo.cuisines,
        navDestination = Navigation.SEND_TO_MEAL_DETAIL,
        actions = ingredientActions
    ) {
        mealItem = null
        mealInfoLiveDataHandler.restoreFromValue(mealInfo)
    }

    constructor(
        mealItem: MealItem,
        ingredientActions: List<ItemAction>
    ) : super(
        source = mealItem.source,
        restaurantId = mealItem.restaurantSubmissionId,
        navDestination = Navigation.SEND_TO_MEAL_DETAIL,
        actions = ingredientActions
    ) {
        this.mealItem = mealItem
    }

    constructor(ingredientActions: List<ItemAction>) : super(
        source = null,
        navDestination = Navigation.SEND_TO_MEAL_DETAIL,
        actions = ingredientActions
    ) {
        mealItem = null
    }

    constructor(parcel: Parcel) : super(parcel) {
        mealItem = parcel.readParcelable(MealItem::class.java.classLoader)
        mealInfoLiveDataHandler.restoreFromParcel(parcel, MealInfo::class)
    }

    override fun update() {
        if (mealInfoLiveDataHandler.tryRestore()) {
            return
        }
        if (mealItem!!.source == Source.API || mealItem.source == Source.FAVORITE) {
            if (mealItem.restaurantSubmissionId != null) {
                mealRepository.getApiRestaurantMealInfo(
                    restaurantId = mealItem.restaurantSubmissionId,
                    mealId = requireNotNull(mealItem.submissionId),
                    userSession = getUserSession(),
                    success = mealInfoLiveDataHandler::set,
                    error = onError
                )
            } else {
                mealRepository.getApiMealInfo(
                    mealId = requireNotNull(mealItem.submissionId),
                    userSession = getUserSession(),
                    success = mealInfoLiveDataHandler::set,
                    error = onError
                )
            }
        } else {
            fetchDbBySource(requireNotNull(mealItem.dbId), requireNotNull(source) {
                "Cannot find meal info from db without a source!"
            })
        }
    }

    private fun fetchDbBySource(dbId: Long, source: Source) {
        mealInfoLiveDataHandler.set(
            mealRepository.getByIdAndSource(
                dbId = dbId,
                source = source
            )
        ) { dbDto ->
            mealRepository.dbMealInfoMapper.mapToModel(dbDto).also { mapped ->
                onMealInfo(mapped)
            }
        }
    }

    fun observeInfo(owner: LifecycleOwner, observer: (MealInfo) -> Unit) {
        mealInfoLiveDataHandler.observe(owner) { mealInfo ->
            onMealInfo(mealInfo)
            observer(mealInfo)
        }
    }

    private fun onMealInfo(mealInfo: MealInfo) {
        super.restoreFromList(mealInfo.mealComponents.plus(mealInfo.ingredientComponents))
        super.tryRestore()
    }

    fun setVote(
        vote: VoteState,
        userSession: UserSession,
        onSuccess: () -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        mealRepository.putVote(
            restaurantId = requireNotNull(mealInfo!!.restaurantSubmissionId),
            mealId = requireNotNull(mealInfo!!.submissionId),
            vote = vote,
            success = {
                mealInfo?.votes?.userHasVoted = vote
                onSuccess()
            },
            error = onError,
            userSession = userSession
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeParcelable(mealItem, flags)
        mealInfoLiveDataHandler.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun removeObservers(owner: LifecycleOwner) {
        liveDataHandler.removeObservers(owner)
    }

    companion object CREATOR : Parcelable.Creator<RestaurantListViewModel> {

        override fun createFromParcel(parcel: Parcel): RestaurantListViewModel =
            TODO("Restore RestaurantRecyclerViewModel from bundle")

        override fun newArray(size: Int): Array<RestaurantListViewModel?> {
            return arrayOfNulls(size)
        }
    }
}