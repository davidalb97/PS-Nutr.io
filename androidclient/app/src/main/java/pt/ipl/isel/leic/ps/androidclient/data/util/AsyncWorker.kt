package pt.ipl.isel.leic.ps.androidclient.data.util

import android.os.AsyncTask
import android.util.Log
import pt.ipl.isel.leic.ps.androidclient.TAG

class AsyncWorker<P, R>(val func: (Array<out P?>) -> R) : AsyncTask<P, Int, R>() {

    constructor(func: (Array<out P?>) -> R, onError: (Exception) -> Unit) : this(func) {
        this.onError = onError
    }

    private var onError: (Exception) -> Unit = { Log.e(TAG, it.message, it) }
    private var onPostExecuteFunc: (R) -> Unit = { }

    override fun doInBackground(vararg params: P?): R {
        return try {
            func(params)
        } catch (e: Exception) {
            onError(e)
            throw e
        }
    }

    fun setOnPostExecute(onSuccess: (R) -> Unit): AsyncWorker<P, R> {
        onPostExecuteFunc = onSuccess
        return this
    }

    override fun onPostExecute(result: R) = onPostExecuteFunc(result)
}