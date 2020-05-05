package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import pt.ipl.isel.leic.ps.androidclient.data.repo.HttpServerRepository

const val TAG = "Nutr.io App"

/**
 * The application context.
 * Starts the Volley request queue and the
 * application's repositories.
 */
class NutrioApp : Application() {
    // App instances and initializations

    lateinit var app: Context
    lateinit var requestQueue: RequestQueue
    val httpServerRepository
            by lazy { HttpServerRepository(app, requestQueue) }

    override fun onCreate() {
        super.onCreate()
        app = this
        requestQueue = Volley.newRequestQueue(app)
    }

}