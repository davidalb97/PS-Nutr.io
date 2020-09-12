package pt.ipl.isel.leic.ps.androidclient.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.encryptedSharedPreferences
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getEmail
import pt.ipl.isel.leic.ps.androidclient.ui.util.getIsFirstTime
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUsername

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val log = Logger(MainActivity::class.java)

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.splash_screen)

        handler = Handler()

        val isFirstTime: Boolean = sharedPreferences.getIsFirstTime()
        log.v("isFirstTime : $isFirstTime")

        if (isFirstTime) {
            handler.postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        } else {
            setContentView(R.layout.activity_main)
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)

            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_insulin_profiles_nested,
                    R.id.nav_settings,
                    R.id.nav_about,
                    R.id.nav_tab_meals,
                    R.id.nav_tab_your_meals,
                    R.id.nav_calculator,
                    R.id.nav_history,
                    R.id.nav_restaurant_detail,
                    R.id.nav_meal_detail,
                    R.id.nav_map,
                    R.id.nav_restaurants,
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

            val registeredUser = encryptedSharedPreferences.getUsername()
            val registeredEmail = encryptedSharedPreferences.getEmail()

            if (registeredUser != null && registeredEmail != null) {
                username.text = registeredUser
                userEmail.text = registeredEmail
            }

            headerImage.setOnClickListener {
                navController.navigate(R.id.nav_sign)
            }
        }
        log.v("onCreate() called!")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
