package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.ingredientRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

open class IngredientRecyclerViewModel() : MealRecyclerViewModel() {
    var ingredients: List<MealIngredient>? = null

    constructor(parcel: Parcel) : this()

    override fun update() {
        if (ingredients != null) {
            liveDataHandler.set(ingredients!!)
        } else {
            ingredientRepository.getIngredients(
                count = count,
                skip = skip,
                success = liveDataHandler::add,
                error = onError
            )
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        //TODO
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<IngredientRecyclerViewModel> {

        override fun createFromParcel(parcel: Parcel): IngredientRecyclerViewModel {
            return IngredientRecyclerViewModel(parcel)
        }

        override fun newArray(size: Int): Array<IngredientRecyclerViewModel?> {
            return arrayOfNulls(size)
        }

    }

}