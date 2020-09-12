package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pt.ipl.isel.leic.ps.androidclient.R

const val SECRET_PREFERENCES = "encryptedPreferences"
const val PREFERENCES = "preferences"

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        preferenceManager.sharedPreferencesName = PREFERENCES

        addPreferencesFromResource(R.xml.preferences)
    }
}
