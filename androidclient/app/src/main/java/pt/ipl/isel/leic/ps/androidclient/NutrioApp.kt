package pt.ipl.isel.leic.ps.androidclient

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.view.Menu
import android.widget.Toast
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.*
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.db.NutrioDb
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.repo.*
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.PREFERENCES
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.SECRET_PREFERENCES
import pt.ipl.isel.leic.ps.androidclient.ui.util.*

const val TAG = "Nutr.io App"
const val ROOM_DB_NAME = "nutrio-db"
const val ROOM_DB_VERSION = 36

/**
 * The application context.
 * ----------------------------------------------------------------
 * Starts the application's data source, repositories and services.
 */
class NutrioApp : Application() {

    private val log = Logger(NutrioApp::class)

    companion object {
        var menu: Menu? = null

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

        authenticateUser()
    }

    private fun initSharedPreferences() {
        sharedPreferences =
            applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
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

    private fun authenticateUser() {

        val email = encryptedSharedPreferences.getEmail()
        val password = encryptedSharedPreferences.getPassWord()

        if (email != null && password != null) {
            userRepository.loginUser(
                userLogin = UserLogin(email, password),
                onSuccess = { userSession ->
                    userRepository.requestUserInfo(
                        userSession = userSession,
                        onSuccess = { userInfo ->
                            saveSession(
                                jwt = userSession.jwt,
                                email = userInfo.email,
                                username = userInfo.username,
                                password = password
                            )
                        },
                        onError = {
                            deleteSession()
                            log.e(it)
                            Toast.makeText(
                                app,
                                R.string.user_info_error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                onError = {
                    log.e(it)
                    deleteSession()
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