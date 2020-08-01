package pt.ipl.isel.leic.ps.androidclient

import android.content.Context
import android.util.Log
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
        // TODO
        Log.e(TAG, "Should fetch and synchronize user related data...")
        return Result.success()
    }
}