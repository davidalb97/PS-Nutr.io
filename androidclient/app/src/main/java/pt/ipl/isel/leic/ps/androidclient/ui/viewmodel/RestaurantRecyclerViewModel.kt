package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.COUNT

class RestaurantRecyclerViewModel(
    private val app: NutrioApp
) : ARecyclerViewModel<Restaurant>() {


    fun getRestaurants(
        onSuccess: (List<Restaurant>) -> Unit,
        onError: () -> Unit
    ) {
        app.restaurantRepository.getRestaurants(
            onSuccess,
            onError,
            COUNT,
            skip
        )
    }

    override fun fetchLiveData(): LiveData<List<Restaurant>>? {
        TODO()
    }
}
