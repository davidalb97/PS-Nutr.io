package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.requireUserSession
import kotlin.reflect.KClass

open class MealItemListViewModel : BaseMealListViewModel<MealItem> {

    constructor(
        navDestination: Navigation,
        actions: List<ItemAction>,
        source: Source?,
        restaurantId: String? = null,
        cuisines: List<Cuisine> = emptyList()
    ) : super(
        navDestination = navDestination,
        actions = actions,
        source = source,
        restaurantId = restaurantId,
        cuisines = cuisines
    )

    constructor(parcel: Parcel) : super(parcel)

    override fun fetch() {
        when (source) {
            Source.API -> {
                val restaurantId = restaurantId
                if (restaurantId != null) {
                    mealRepository.getRestaurantMealItems(
                        restaurantId = restaurantId,
                        count = count,
                        skip = skip,
                        userSession = getUserSession(),
                        success = liveDataHandler::add,
                        error = onError
                    )
                } else {
                    mealRepository.getMealItems(
                        count = count,
                        skip = skip,
                        cuisines = cuisines,
                        userSession = getUserSession(),
                        success = liveDataHandler::add,
                        error = onError
                    )
                }
            }
            Source.FAVORITE_MEAL -> mealRepository.getFavoriteMeals(
                userSession = requireUserSession(),
                count = count,
                skip = skip,
                success = liveDataHandler::add,
                error = onError
            )
            Source.FAVORITE_RESTAURANT_MEAL -> mealRepository.getFavoriteRestaurantMeals(
                userSession = requireUserSession(),
                count = count,
                skip = skip,
                success = liveDataHandler::add,
                error = onError
            )
            Source.CUSTOM_MEAL -> mealRepository.getCustomMeals(
                userSession = requireUserSession(),
                count = count,
                skip = skip,
                success = liveDataHandler::add,
                error = onError
            )
            else -> fetchDbInfoBySource(requireNotNull(source) {
                "Cannot meal items from db without a source!"
            })
        }
    }

    private fun fetchDbInfoBySource(source: Source) {
        this.liveDataHandler.set(mealRepository.getAllItemBySource(source)) {
            mealRepository.dbMealItemMapper.mapToModel(it)
        }
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<MealItemListViewModel> {

        override fun createFromParcel(parcel: Parcel): MealItemListViewModel {
            return MealItemListViewModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<MealItemListViewModel?> {
            return arrayOfNulls(size)
        }

    }

    override fun getModelClass(): KClass<MealItem> = MealItem::class

}