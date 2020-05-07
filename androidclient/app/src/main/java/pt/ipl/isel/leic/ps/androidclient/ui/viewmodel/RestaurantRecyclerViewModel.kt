package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

class RestaurantRecyclerViewModel(
    private val app: NutrioApp
) : ARecyclerViewModel<Restaurant>() {

    private val count = 10
    private var skip = 0

    fun getRestaurants(
        onSuccess: (List<Restaurant>) -> Unit,
        onError: () -> Unit
    ) {
        app.restaurantRepository.getRestaurants(
            onSuccess,
            onError,
            count,
            skip
        )
    }

    override fun fetchLiveData(): LiveData<List<Restaurant>>? {
        TODO()
    }
}
