package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.work.*
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.*
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.db.NutrioDb
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.repo.*
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.*
import java.util.concurrent.TimeUnit

const val TAG = "Nutr.io App"
const val ROOM_DB_NAME = "nutrio-db"
const val ROOM_DB_VERSION = 25

/**
 * The application context.
 * ----------------------------------------------------------------
 * Starts the application's data source, repositories and services.
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
         * Room database
         */
        lateinit var roomDb: NutrioDb

        /**
         * Shared preferences
         */
        lateinit var sharedPreferences: SharedPreferences

        /**
         * Encrypted shared preferences
         */
        lateinit var encryptedSharedPreferences: SharedPreferences

        /**
         *  Repositories initialization
         */
        val restaurantRepository by lazy {
            RestaurantRepository(RestaurantDataSource(requester))
        }

        val mealRepository by lazy { MealRepository(MealDataSource(requester)) }

        val cuisineRepository by lazy {
            CuisineRepository(CuisineDataSource(requester))
        }
        val insulinProfilesRepository by lazy {
            InsulinProfileRepository(InsulinProfileDataSource(requester))
        }

        val userRepository by lazy {
            UserRepository(UserDataSource(requester))
        }
        val ingredientRepository by lazy {
            IngredientRepository(IngredientDataSource(requester))
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = applicationContext
        roomDb = Room.databaseBuilder(applicationContext, NutrioDb::class.java, ROOM_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

        initSharedPreferences()
        initEncryptedSharedPreferences()
        initPeriodicWorker()

        authenticateUser()
    }

    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun initEncryptedSharedPreferences() {
        val masterKey = MasterKey
            .Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedSharedPreferences =
            EncryptedSharedPreferences.create(
                applicationContext,
                SECRET_PREFERENCES,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }

    private fun initPeriodicWorker() {

        // Device constraints for the periodic worker
        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkerRequest: PeriodicWorkRequest =
            PeriodicWorkRequest
                .Builder(PeriodicWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkerRequest
            )
    }

    private fun authenticateUser() {

        val username = encryptedSharedPreferences.getString(USERNAME, null)
        val password = encryptedSharedPreferences.getString(PASSWORD, null)

        if (username != null && password != null) {
            userRepository.loginUser(
                UserLogin(username, password),
                { userSession -> saveSession(userSession.jwt, username, password) },
                {
                    Toast.makeText(
                        app,
                        getString(R.string.login_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}

/**
 * Saves the user credentials into the encrypted shared preferences
 * and the JSON Web Token into the shared preferences.
 *
 * @param jwt - The JSON Web Token
 * @param username - The username
 * @param password - The password
 */
fun saveSession(jwt: String, username: String, password: String) {
    NutrioApp.encryptedSharedPreferences
        .edit()
        .putString(USERNAME, username)
        .putString(PASSWORD, password)
        .apply()
    NutrioApp.sharedPreferences.edit()
        .putString(JWT, jwt)
        .apply()
}

/**
 * Checks the internet connectivity
 */
fun hasInternetConnection(): Boolean {
    val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var hasInternet = false
    val networks: Array<Network> = cm.allNetworks

    if (networks.isNotEmpty()) {
        for (network in networks) {
            val nc = cm.getNetworkCapabilities(network)
            if (nc?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true)
                hasInternet = true
        }
    }

    return hasInternet
}

fun Fragment.withCtx(work: () -> Unit) {
    if (this.isAdded) {
        work
    }
}