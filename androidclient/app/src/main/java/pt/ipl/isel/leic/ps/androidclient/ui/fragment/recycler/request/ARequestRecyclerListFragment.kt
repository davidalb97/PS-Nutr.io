package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.ARecyclerListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

abstract class ARequestRecyclerListFragment<M : Any, VM : ARecyclerViewModel<M>>
    : ARecyclerListFragment<M, VM>() {

    lateinit var progressWheel: ProgressBar

    override fun initRecyclerList(view: View) {
        this.list = view.findViewById(getRecyclerId())

        // List progressBar
        this.progressWheel = view.findViewById(getProgressBarId())

        this.progressWheel.visibility = View.VISIBLE
    }

    override fun successFunction(list: List<M>) {
        if (list.isEmpty() && this.isAdded)
            Toast.makeText(
                activityApp, R.string.no_result_found,
                Toast.LENGTH_LONG
            ).show()
        this.progressWheel.visibility = View.INVISIBLE
        Log.v(TAG, "running on the thread : ${Thread.currentThread().name}")
    }

    override fun errorFunction(exception: Throwable) {
        if (this.isAdded) {
            if (!hasInternetConnection(activityApp as NutrioApp)) {
                Toast.makeText(
                    activityApp, R.string.no_internet_connection,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(activityApp, R.string.error_network, Toast.LENGTH_LONG)
                    .show()
            }
        }
        this.progressWheel.visibility = View.INVISIBLE
        Log.v(TAG, exception.message, exception)
    }
}