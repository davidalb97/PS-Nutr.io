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

    fun getRestaurants() {
        app.httpServerRepository.getRestaurants(
            successFunction(),
            errorFunction(),
            10,
            10
        )
    }

    override fun fetchLiveData(): LiveData<List<Restaurant>>? {
        TODO("Not yet implemented")
    }

    private fun successFunction(): (List<Restaurant>) -> Unit = {
        val onSuccess: (ArrayList<Restaurant>) -> Unit = ::getItems
        if (it.isEmpty())
            Toast.makeText(
                app, R.string.no_result_found,
                Toast.LENGTH_LONG
            ).show()
        Log.v(TAG, "running on the thread : ${Thread.currentThread().name}")
    }

    private fun errorFunction(): () -> Unit = {
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
