package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import kotlin.reflect.KClass

abstract class BaseMealListViewModel<M : Parcelable> : BaseListViewModel<M> {

    val navDestination: Navigation
    val actions: List<ItemAction>
    val source: Source?
    var restaurantId: String?
    val cuisines: List<Cuisine>

    constructor(
        itemClass: KClass<M>,
        navDestination: Navigation,
        actions: List<ItemAction>,
        source: Source?,
        restaurantId: String? = null,
        cuisines: List<Cuisine> = emptyList()
    ) : super(itemClass) {
        this.navDestination = navDestination
        this.actions = actions
        this.source = source
        this.restaurantId = restaurantId
        this.cuisines = cuisines
    }

    constructor(parcel: Parcel, itemClass: KClass<M>) : super(parcel, itemClass) {
        this.navDestination = Navigation.values()[parcel.readInt()]
        this.actions = parcel.readListCompat(ItemAction::class)
        this.source = (parcel.readSerializable() as Int?)?.let { Source.values()[it] }
        this.restaurantId = parcel.readString()
        this.cuisines = parcel.readListCompat(Cuisine::class)
    }

    fun insertInfo(mealInfo: MealInfo) = mealRepository.insertInfo(mealInfo)

    fun deleteInfoById(dbMealId: Long) = mealRepository.deleteInfoById(dbMealId)

    fun deleteInfo(mealInfo: MealInfo) = mealRepository.deleteInfo(mealInfo)

    fun insertItem(mealItem: MealItem) = mealRepository.insertItem(mealItem)

    fun deleteItem(mealItem: MealItem) = mealRepository.deleteItem(mealItem)

    fun deleteItemById(dbMealId: Long) = mealRepository.deleteItemById(dbMealId)

    fun report(
        mealItem: MealItem,
        reportStr: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) = mealRepository.report(
        restaurantId = requireNotNull(mealItem.restaurantSubmissionId),
        mealId = requireNotNull(mealItem.submissionId),
        reportMsg = reportStr,
        success = onSuccess,
        error = onError,
        userSession = requireUserSession()
    )

    fun putFavorite(
        mealItem: MealItem,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {

        val favorites = requireNotNull(mealItem.favorites) {
            "Favorite can not be null"
        }
        val submissionId = requireNotNull(mealItem.submissionId) {
            "Submission"
        }

        mealRepository.putFavorite(
            restaurantId = mealItem.restaurantSubmissionId,
            submissionId = submissionId,
            isFavorite = !favorites.isFavorite,
            success = {
                favorites.isFavorite = !favorites.isFavorite
                onSuccess()
            },
            error = onError,
            userSession = requireUserSession()
        )
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeInt(navDestination.ordinal)
        dest?.writeList(actions)
        dest?.writeSerializable(source?.ordinal)
        dest?.writeString(restaurantId)
        dest?.writeList(cuisines)
    }
}