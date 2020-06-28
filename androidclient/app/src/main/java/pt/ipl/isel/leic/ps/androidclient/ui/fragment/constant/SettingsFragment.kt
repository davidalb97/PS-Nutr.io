package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import pt.ipl.isel.leic.ps.androidclient.DARK_MODE
import pt.ipl.isel.leic.ps.androidclient.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        setupDarkModeSwitch()
    }

    @SuppressLint("CommitPrefEdits")
    private fun setupDarkModeSwitch() {
        val darkModeSwitch = findPreference<SwitchPreferenceCompat>(DARK_MODE)
        val sharedPreferences: SharedPreferences =
            requireActivity().baseContext?.getSharedPreferences(
                "preferences.xml",
                Context.MODE_PRIVATE
            )!!

        val isLightMode = sharedPreferences.getBoolean(DARK_MODE, false)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        if (!isLightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        darkModeSwitch!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener() { preference: Preference, value: Any ->
                if (!(value as Boolean)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean(DARK_MODE, false)
                    editor.apply()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean(DARK_MODE, true)
                    editor.apply()
                }
                true;
            }
    }
}
