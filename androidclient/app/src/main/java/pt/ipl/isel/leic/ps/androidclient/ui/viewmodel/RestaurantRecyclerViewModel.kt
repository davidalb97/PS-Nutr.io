package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection

class RestaurantRecyclerViewModel(
    private val app: NutrioApp
) : ARecyclerViewModel<Restaurant>() {

    private val COUNT = 10
    private var skip = 0

    fun getRestaurants() {
        app.httpServerRepository.getRestaurants(
            successFunction(),
            errorFunction(),
            COUNT,
            skip
        )
    }

    override fun fetchLiveData(): LiveData<List<Restaurant>>? {
        TODO("Not yet implemented")
    }

    /**
     * The success function.
     * A Toast will pop up telling no results were found, if a request
     * returns an empty list.
     */
    override fun successFunction(): (List<Restaurant>) -> Unit = {
        val onSuccess: (List<Restaurant>) -> Unit = ::setItems
        if (it.isEmpty())
            Toast.makeText(
                app, R.string.no_result_found,
                Toast.LENGTH_LONG
            ).show()
        Log.v(TAG, "running on the thread : ${Thread.currentThread().name}")
    }

    /**
     * The error function.
     * Pops up a Toast if there's no internet connection
     * or if it couldn't get results.
     */
    override fun errorFunction(): () -> Unit = {
        if (!hasInternetConnection(app)) {
            Toast.makeText(
                app, R.string.no_internet_connection,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(app, R.string.error_network, Toast.LENGTH_LONG)
                .show()
        }
    }
}
