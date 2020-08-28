package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.ingredientRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import kotlin.reflect.KClass

class IngredientPickViewModel : BaseItemPickerViewModel<MealIngredient> {

    var ingredientsChanged = false

    constructor(parcel: Parcel) : super(parcel)

    constructor() : super()

    override fun update() {
        if (!tryRestore()) {
            ingredientRepository.getIngredients(count, skip, liveDataHandler::set, onError)
        }
    }

    override fun getModelClass(): KClass<MealIngredient> = MealIngredient::class

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IngredientPickViewModel> {
        override fun createFromParcel(parcel: Parcel): IngredientPickViewModel {
            return IngredientPickViewModel(parcel)
        }

        override fun newArray(size: Int): Array<IngredientPickViewModel?> {
            return arrayOfNulls(size)
        }
    }
}