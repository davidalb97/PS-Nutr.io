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
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel
import kotlin.reflect.KClass

open class MealInfoViewModel : MealItemListViewModel {

    //TODO Override removeObservers
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
        mealItem = mealInfo
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

    protected constructor() : super(
        source = null,
        navDestination = Navigation.IGNORE,
        actions = emptyList()
    ) {
        mealItem = null
    }

    constructor(parcel: Parcel) : super(parcel) {
        mealItem = parcel.readParcelable(MealItem::class.java.classLoader)
        mealInfoLiveDataHandler.restoreFromParcel(parcel, MealInfo::class)
    }

    override fun setupList() {
        if (mealInfo != null) {
            mealInfoLiveDataHandler.notifyChanged()
        } else triggerFetch()
    }

    override fun tryRestore(): Boolean {
        return mealInfoLiveDataHandler.tryRestore()
    }

    override fun fetch() {
        if (mealItem!!.restaurantSubmissionId != null) {
            mealRepository.getApiRestaurantMealInfo(
                restaurantId = mealItem.restaurantSubmissionId!!,
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
    }

    fun observeInfo(owner: LifecycleOwner, observer: (MealInfo) -> Unit) {
        mealInfoLiveDataHandler.observe(owner) { mealInfo ->
            onMealInfo(mealInfo)
            observer(mealInfo)
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        mealInfoLiveDataHandler.removeObservers(owner)
        super.removeObservers(owner)
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

    fun addMealPortion(
        restaurantId: String,
        mealId: Int,
        portion: Portion,
        userSession: UserSession,
        onSuccess: (Int) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        mealRepository.addMealPortion(
            restaurantId = restaurantId,
            mealId = mealId,
            portion = portion,
            userSession = userSession,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun editMealPortion(
        restaurantId: String,
        mealId: Int,
        portion: Portion,
        userSession: UserSession,
        onSuccess: (Int) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        mealRepository.editMealPortion(
            restaurantId = restaurantId,
            mealId = mealId,
            portion = portion,
            userSession = userSession,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun deleteMealPortion(
        restaurantId: String,
        mealId: Int,
        userSession: UserSession,
        onSuccess: (Int) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        mealRepository.deleteMealPortion(
            restaurantId = restaurantId,
            mealId = mealId,
            userSession = userSession,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeParcelable(mealItem, flags)
        mealInfoLiveDataHandler.writeToParcel(dest, flags)
    }

    override fun getModelClass(): KClass<MealItem> = MealItem::class

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealInfoViewModel> {

        override fun createFromParcel(parcel: Parcel): MealInfoViewModel =
            TODO("Restore RestaurantRecyclerViewModel from bundle")

        override fun newArray(size: Int): Array<MealInfoViewModel?> {
            return arrayOfNulls(size)
        }
    }
}