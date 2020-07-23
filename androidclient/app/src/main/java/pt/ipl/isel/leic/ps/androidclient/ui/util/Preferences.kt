package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.PREFERENCES_FILE

fun Fragment.requireSharedPreferences(): SharedPreferences {
    return this.requireActivity().baseContext?.getSharedPreferences(
        PREFERENCES_FILE,
        Context.MODE_PRIVATE
    )!!
}