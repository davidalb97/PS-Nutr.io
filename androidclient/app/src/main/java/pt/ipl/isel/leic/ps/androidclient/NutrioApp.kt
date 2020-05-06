package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import pt.ipl.isel.leic.ps.androidclient.data.repo.HttpServerRepository

const val TAG = "Nutr.io App"

/**
 * The application context.
 * ---------------------------------------
 * Starts the the application's repositories.
 */
class NutrioApp : Application() {

    /**
     * Application context
     */
    lateinit var app: Context

    /**
     *  Repositories initialization
     */
    val httpServerRepository
            by lazy { HttpServerRepository(app) }

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