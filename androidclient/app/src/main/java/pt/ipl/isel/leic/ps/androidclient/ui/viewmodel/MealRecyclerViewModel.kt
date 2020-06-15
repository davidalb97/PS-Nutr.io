package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem

open class MealRecyclerViewModel(
    var restaurantId: String?
) : ARecyclerViewModel<MealItem>() {

    var cuisines = emptyList<Cuisine>()

    fun addToFavorite(mealItem: MealItem) =
        mealRepository.insertItem(mealItem)

    constructor(parcel: Parcel): this(
        restaurantId = TODO()
//        restaurantId = parcel.readString()
    )

    override fun update() {
        if(restaurantId != null) {
            mealRepository.getRestaurantMealItems(
                restaurantId = restaurantId!!,
                count = count,
                skip = skip,
                success = liveDataHandler::add,
                error = onError
            )
        } else {
            mealRepository.getMealItems(
                count = count,
                skip = skip,
                cuisines = cuisines,
                success = liveDataHandler::add,
                error = onError
            )
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
//        dest?.writeString(restaurantId)
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    companion object CREATOR : Parcelable.Creator<MealRecyclerViewModel> {

        override fun createFromParcel(parcel: Parcel): MealRecyclerViewModel {
            return MealRecyclerViewModel(parcel)
        }
        override fun newArray(size: Int): Array<MealRecyclerViewModel?> {
            return arrayOfNulls(size)
        }

    }

}