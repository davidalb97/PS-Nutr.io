package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.util.Log
import pt.ipl.isel.leic.ps.androidclient.TAG
import kotlin.reflect.KClass

class Logger(private val subject: String) {

    constructor(klass: KClass<*>) : this(klass.simpleName!!.toString())

    constructor(clazz: Class<*>) : this(clazz.simpleName)

    fun v(msg: String) {
        logv1(subject, msg)
    }

    fun e(throwable: Throwable) {
        Log.e(TAG, "[$subject]: ${throwable.message}", throwable)
        throwable.printStackTrace()
        throw throwable
    }

    fun e(msg: String, throwable: Throwable) {
        Log.e(TAG, "[$subject]: $msg", throwable)
        throwable.printStackTrace()
        throw throwable
    }
}

fun logv(msg: String) {
    Log.v(TAG, "**** $msg")
}

fun logv1(subject: String, msg: String) {
    Log.v(TAG, "[$subject]: $msg")
}
