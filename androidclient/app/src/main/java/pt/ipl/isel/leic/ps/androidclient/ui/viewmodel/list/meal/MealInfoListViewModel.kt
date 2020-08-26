package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import kotlin.reflect.KClass

open class MealInfoListViewModel : BaseMealListViewModel<MealInfo> {

    constructor(
        navDestination: Navigation,
        actions: List<ItemAction>,
        source: Source?,
        restaurantId: String? = null,
        cuisines: List<Cuisine> = emptyList(),
        checkedItems: List<MealInfo> = emptyList()
    ) : super(
        navDestination = navDestination,
        actions = actions,
        source = source,
        restaurantId = restaurantId,
        cuisines = cuisines,
        checkedItems = checkedItems
    )

    constructor(parcel: Parcel) : super(parcel)

    override fun update() {
        if (!super.tryRestore()) {
            when (source) {
                Source.API -> throw UnsupportedOperationException(
                    "API info meal list is not supported!"
                )
                else -> fetchDbBySource(requireNotNull(source) {
                    "Cannot find meal info from db without a source!"
                })
            }
        }
    }

    private fun fetchDbBySource(source: Source) {
        this.liveDataHandler.set(mealRepository.getAllInfoBySource(source)) {
            mealRepository.dbMealInfoMapper.mapToModel(it)
        }
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<MealInfoListViewModel> {

        override fun createFromParcel(parcel: Parcel): MealInfoListViewModel {
            return MealInfoListViewModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<MealInfoListViewModel?> {
            return arrayOfNulls(size)
        }

    }

    override fun getModelClass(): KClass<MealInfo> = MealInfo::class

}