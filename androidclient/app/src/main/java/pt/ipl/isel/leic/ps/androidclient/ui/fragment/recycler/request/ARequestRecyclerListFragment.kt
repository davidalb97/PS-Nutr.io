package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.ARecyclerListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

abstract class ARequestRecyclerListFragment<M : Any, VM : ARecyclerViewModel<M>>
    : ARecyclerListFragment<M, VM>() {

    lateinit var progressBar: ProgressBar

    override fun initRecyclerList(view: View) {
        this.list =
            view.findViewById(R.id.itemList) as RecyclerView

        // List progressBar
        this.progressBar =
            view.findViewById(R.id.progressBar) as ProgressBar

        this.progressBar.visibility = View.VISIBLE


    }

    override fun startObserver() {
        viewModel.observe(this) {
            list.adapter?.notifyDataSetChanged()
            if (viewModel.mediatorLiveData.value!!.isEmpty()) {
                Toast.makeText(
                    this.requireContext(),
                    R.string.no_result_found,
                    Toast.LENGTH_LONG
                ).show()
            }
            this.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun successFunction(list: List<M>) {
        if (list.isEmpty() && this.isAdded)
            Toast.makeText(
                activityApp, R.string.no_result_found,
                Toast.LENGTH_LONG
            ).show()
        Log.v(TAG, "running on the thread : ${Thread.currentThread().name}")
    }

    override fun errorFunction(exception: Exception) {
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
        this.progressBar.visibility = View.INVISIBLE
        Log.v(TAG, exception.message, exception)
    }
}