package pt.ipl.isel.leic.ps.androidclient

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * The app's periodic worker.
 * ----------------------------
 * Does background job when the phone
 * is inactive and connected to the internet
 */
class PeriodicWorker(
    ctx: Context,
    workerParams: WorkerParameters
) : Worker(ctx, workerParams) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}