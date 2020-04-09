package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import com.android.volley.toolbox.Volley
import pt.ipl.isel.leic.ps.androidclient.data.Repository

const val TAG = "Nutr.io App"

class NutrioApp : Application() {
    // App instances and initializations
    val app = this
    lateinit var repo: Repository

    override fun onCreate() {
        super.onCreate()
        repo = Repository(
            app,
            Volley.newRequestQueue(app) // Volley queue
        )
    }

}