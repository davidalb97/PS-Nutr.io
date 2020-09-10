package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.ingredientRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

private val ITEM_CLASS = MealIngredient::class

open class IngredientListViewModel : BaseMealListViewModel<MealIngredient> {

    constructor(
        navDestination: Navigation,
        actions: List<ItemAction>
    ) : super(
        itemClass = ITEM_CLASS,
        navDestination = navDestination,
        actions = actions,
        source = Source.API
    )

    constructor(parcel: Parcel) : super(parcel, ITEM_CLASS)

    override fun fetch() {
        when (source) {
            Source.API -> ingredientRepository.getIngredients(
                count = count,
                skip = skip,
                success = liveDataHandler::add,
                error = onError
            )
            else -> throw UnsupportedOperationException("Can only obtain ingredients from API!")
        }
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<IngredientListViewModel> {

        override fun createFromParcel(parcel: Parcel) = IngredientListViewModel(parcel)

        override fun newArray(size: Int): Array<IngredientListViewModel?> {
            return arrayOfNulls(size)
        }
    }
}