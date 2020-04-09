package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import pt.ipl.isel.leic.ps.androidclient.data.repositories.HttpServerRepository

const val TAG = "Nutr.io App"

class NutrioApp : Application() {
    // App instances and initializations

    companion object {
        lateinit var app: Context
        lateinit var requestQueue: RequestQueue
        val serverRepo by lazy { HttpServerRepository() }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        requestQueue = Volley.newRequestQueue(app)
    }

}