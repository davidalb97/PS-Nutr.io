package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.util.Log
import pt.ipl.isel.leic.ps.androidclient.TAG
import kotlin.reflect.KClass

class Logger(private val subject: String) {

    constructor(clazz: KClass<*>) : this(clazz.simpleName!!.toString())

    constructor(clazz: Class<*>) : this(clazz.simpleName)

    fun v(msg: String) {
        Log.v(TAG, "[$subject]: $msg")
    }

    fun e(throwable: Throwable) {
        Log.e(TAG, "[$subject]: ${throwable.message}", throwable)
    }

    fun e(msg: String, throwable: Throwable) {
        Log.e(TAG, "[$subject]: $msg", throwable)
    }
}