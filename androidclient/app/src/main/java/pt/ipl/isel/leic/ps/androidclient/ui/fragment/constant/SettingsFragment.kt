package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pt.ipl.isel.leic.ps.androidclient.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}
