package pt.ipl.isel.leic.ps.androidclient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.SwitchPreferenceCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import java.util.concurrent.TimeUnit

const val DARK_MODE = "dark_mode"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val log = Logger(MainActivity::class.java)

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        handler = Handler()

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE)!!

        val isFirstTime: Boolean? = sharedPreferences.getBoolean("isFirstTime", false)

        if (isFirstTime == null || isFirstTime) {
            handler.postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        } else {
            setContentView(R.layout.activity_main)
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)

            // Check current theme
            val isLightMode = sharedPreferences.getBoolean(DARK_MODE, false)

            if (!isLightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                setTheme(R.style.DarkTheme)
            else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.AppTheme)

            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_profile,
                    R.id.nav_settings,
                    R.id.nav_about,
                    R.id.nav_tab_restaurants,
                    R.id.nav_tab_meals,
                    R.id.nav_tab_your_meals,
                    R.id.nav_calculator,
                    R.id.nav_history,
                    R.id.nav_restaurant_detail,
                    R.id.nav_meal_detail,
                    R.id.nav_map,
                    R.id.nav_add_meal_to_calculator,
                    R.id.nav_add_insulin,
                    R.id.nav_saved_meals,
                    R.id.nav_add_custom_meal,
                    R.id.nav_sign,
                    R.id.nav_register,
                    R.id.nav_login,
                    R.id.nav_ingredients
                ),
                drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            val headerView = navView.getHeaderView(0)
            val headerImage = headerView.findViewById<ImageView>(R.id.imageView)

            val username = headerView.findViewById<TextView>(R.id.username)
            val userEmail = headerView.findViewById<TextView>(R.id.userEmail)

            if (sharedPreferences.getString("username", "") != null) {
                username.text = sharedPreferences.getString("username", "")
                userEmail.visibility = View.INVISIBLE
            }

            //TODO - change when bottom bar is working
            headerImage.setOnClickListener {
                navController.navigate(R.id.nav_login)
            }

            // Periodic worker to sync user related data
            val periodicWorkerRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
                PeriodicWorker::class.java, 1, TimeUnit.DAYS
            ).build()

            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkerRequest)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

/*    // TODO
    override fun onBackPressed() {
        log.v("Called onBackPressed()")
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }*/
}
