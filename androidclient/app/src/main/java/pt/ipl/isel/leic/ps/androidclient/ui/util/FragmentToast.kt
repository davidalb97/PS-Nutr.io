package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

class FragmentToast() {
    constructor(
        fragmentContext: Fragment,
        @StringRes message: Int,
        duration: Int
    ) : this() {
        if (fragmentContext.isAdded) {
            Toast.makeText(fragmentContext.context, message, duration).show()
        }
    }
}