package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.UriBuilder
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.CuisineDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.db.NutrioDb
import pt.ipl.isel.leic.ps.androidclient.data.repo.CuisineRepository
import pt.ipl.isel.leic.ps.androidclient.data.repo.InsulinProfileRepository
import pt.ipl.isel.leic.ps.androidclient.data.repo.MealRepository
import pt.ipl.isel.leic.ps.androidclient.data.repo.RestaurantRepository

const val TAG = "Nutr.io App"
const val ROOM_DB_NAME = "nutrio-db"
const val ROOM_DB_VERSION = 15

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
            RequestParser(
                Volley.newRequestQueue(app),
                jacksonObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            )
        }

        /**
         * Uri Builder
         */
        private val uriBuilder = UriBuilder()

        /**
         * Room database
         */
        lateinit var roomDb: NutrioDb

        /**
         *  Repositories initialization
         */
        val restaurantRepository
                by lazy { RestaurantRepository(RestaurantDataSource(requester, uriBuilder)) }
        val mealRepository
                by lazy { MealRepository(MealDataSource(requester, uriBuilder)) }
        val cuisineRepository
                by lazy { CuisineRepository(CuisineDataSource(requester, uriBuilder)) }
        val insulinProfilesRepository
                by lazy { InsulinProfileRepository() }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        roomDb = Room.databaseBuilder(applicationContext, NutrioDb::class.java, ROOM_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
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