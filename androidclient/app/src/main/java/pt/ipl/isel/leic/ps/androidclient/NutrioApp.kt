package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pt.ipl.isel.leic.ps.androidclient.data.repo.CuisineRepository
import pt.ipl.isel.leic.ps.androidclient.data.repo.MealRepository
import pt.ipl.isel.leic.ps.androidclient.data.repo.RestaurantRepository
import pt.ipl.isel.leic.ps.androidclient.data.source.Requester
import pt.ipl.isel.leic.ps.androidclient.data.source.endpoint.CuisineDataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.endpoint.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.endpoint.RestaurantDataSource

const val TAG = "Nutr.io App"

/**
 * The application context.
 * ---------------------------------------
 * Starts the the application's data source & repositories.
 */
class NutrioApp : Application() {

    companion object {
        /**
         * Application context
         */
        lateinit var app: Context

        /**
         * Volley queue and json mapper initialization
         */
        private val requester by lazy {
            Requester(
                Volley.newRequestQueue(app),
                jacksonObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            )
        }

        /**
         * Local databases initialization
         */

        /**
         *  Repositories initialization
         */
        val restaurantRepository
                by lazy { RestaurantRepository(RestaurantDataSource(requester)) }
        val mealRepository
                by lazy { MealRepository(MealDataSource(requester)) }
        val cuisineRepository
                by lazy { CuisineRepository(CuisineDataSource(requester)) }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}

/**
 * Checks the internet connectivity
 */
fun hasInternetConnection(app: NutrioApp): Boolean {
    val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var hasInternet = false
    val networks: Array<Network> = cm.allNetworks

    if (networks.isNotEmpty()) {
        for (network in networks) {
            val nc = cm.getNetworkCapabilities(network)
            if (nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet = true
        }
    }

    return hasInternet
}