package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun closeKeyboard(currentActivity: Activity) {
    val inputMethodManager: InputMethodManager =
        currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentActivity.currentFocus
    if (view != null) {
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}