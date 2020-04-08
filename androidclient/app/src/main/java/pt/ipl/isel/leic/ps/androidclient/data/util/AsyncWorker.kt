package pt.ipl.isel.leic.ps.androidclient.data.util

import android.os.AsyncTask
import android.util.Log
import pt.ipl.isel.leic.ps.androidclient.TAG

class AsyncWorker<P, R>(val func: (Array<out P?>) -> R) : AsyncTask<P, Int, R>() {

    private var onError: (Exception) -> Unit = { e ->
        Log.e(TAG, e.message, e)
    }

    private var onPostExecuteFunc: (R) -> Unit = { }

    override fun doInBackground(vararg params: P?): R {
        return try {
            func(params)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)

            onError(e)
            throw e
        }
    }

    fun setOnPostExecute(onSuccess: (R) -> Unit): AsyncWorker<P, R> {
        this.onPostExecuteFunc = onSuccess
        return this
    }

    override fun onPostExecute(result: R) = onPostExecuteFunc(result)
}