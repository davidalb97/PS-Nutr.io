package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.util.getIsNightMode
import pt.ipl.isel.leic.ps.androidclient.ui.util.setIsNightMode

const val SECRET_PREFERENCES = "encryptedPreferences"
const val PREFERENCES = "preferences.xml"
const val DARK_MODE = "dark_mode"


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        setupDarkModeSwitch()
    }

    private fun setupDarkModeSwitch() {
        val darkModeSwitch = findPreference<SwitchPreferenceCompat>(DARK_MODE)

        val isLightMode = sharedPreferences.getIsNightMode()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        if (!isLightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        darkModeSwitch!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference, value: Any ->
                if (!(value as Boolean)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.setIsNightMode(false)
                    editor.apply()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.setIsNightMode(true)
                    editor.apply()
                }
                true
            }
    }
}
