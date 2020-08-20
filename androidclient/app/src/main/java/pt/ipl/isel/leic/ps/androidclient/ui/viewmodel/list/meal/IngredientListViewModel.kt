package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.ingredientRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

open class IngredientListViewModel : MealInfoListViewModel {

    constructor(
        navDestination: Navigation,
        actions: List<ItemAction>,
        source: Source
    ) : super(
        navDestination = navDestination,
        actions = actions,
        source = source
    )

    constructor(parcel: Parcel) : super(parcel)

    override fun update() {
        if (!tryRestore()) {
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
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<IngredientListViewModel> {

        override fun createFromParcel(parcel: Parcel): IngredientListViewModel {
            return IngredientListViewModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<IngredientListViewModel?> {
            return arrayOfNulls(size)
        }

    }

}