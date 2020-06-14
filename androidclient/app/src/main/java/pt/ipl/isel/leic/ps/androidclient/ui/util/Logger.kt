package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.util.Log
import pt.ipl.isel.leic.ps.androidclient.TAG

class Logger(private val subject: String) {

    fun logv(msg: String) {
        logv(subject, msg)
    }
}

fun logv(msg: String) {
    Log.v(TAG, "**** $msg")
}

fun logv(subject: String, msg: String) {
    Log.v(TAG, "[$subject]: $msg")
}
