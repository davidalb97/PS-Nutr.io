package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.COUNT

class MealRecyclerViewModel(
    val app: NutrioApp
) : ARecyclerViewModel<Meal>() {

    fun getMeals(
        onSuccess: (List<Meal>) -> Unit,
        onError: () -> Unit
    ) {
        app.mealRepository.getMeals(
            onSuccess,
            onError,
            COUNT,
            skip
        )
    }

    override fun fetchLiveData(): LiveData<List<Meal>>? {
        TODO("Not yet implemented")
    }
}