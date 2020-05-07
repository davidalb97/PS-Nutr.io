package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.COUNT

class CuisineRecyclerViewModel(
    private val app: NutrioApp
) : ARecyclerViewModel<Cuisine>() {

    fun getCuisines(
        onSuccess : (List<Cuisine>) -> Unit,
        onError : () -> Unit
    ) {
        app.cuisineRepository.getCuisines(
            onSuccess,
            onError,
            COUNT,
            skip
        )
    }

    override fun fetchLiveData(): LiveData<List<Cuisine>> =
        TODO()

}
