package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine

class CuisineRecyclerViewModel(
    private val app: NutrioApp
) : ARecyclerViewModel<Cuisine>() {

    var skip = 0

    fun getCuisines(
        onSuccess : (List<Cuisine>) -> Unit,
        onError : () -> Unit,
        count: Int
    ) {

        app.cuisineRepository.getCuisines(
            onSuccess,
            onError,
            count,
            skip
        )
    }

    override fun fetchLiveData(): LiveData<List<Cuisine>>? {
        TODO("Not yet implemented")
    }
}
