package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

// Default pagination value
const val COUNT = 10

abstract class ARecyclerListFragment<T : Any, VM : ARecyclerViewModel<T>> : Fragment() {

    lateinit var viewModel: VM
    lateinit var list: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var activityApp: Application

    /**
     * Gets the list elements and initializes them
     */
    fun initRecyclerList(view: View) {
        this.list =
            view.findViewById(R.id.itemList) as RecyclerView

        // List progressBar
        this.progressBar =
            view.findViewById(R.id.progressBar) as ProgressBar

        this.progressBar.visibility = View.VISIBLE

        list.setHasFixedSize(true)
    }

    /**
     * Starts the observer inside the view model
     */
    fun startObserver() {
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

    /**
     * Recycler list scroll listener
     * Receives the fetch from the specific view model
     */
    abstract fun startScrollListener()

    fun setCallbackFunctions() {
        viewModel.onSuccess = this::successFunction
        viewModel.onError = this::errorFunction
    }

    /**
     * The success function.
     * A Toast will pop up telling no results were found, if a request
     * returns an empty list.
     */
    open fun successFunction(list: List<T>) {
        if (list.isEmpty() && this.isAdded)
            Toast.makeText(
                activityApp, R.string.no_result_found,
                Toast.LENGTH_LONG
            ).show()
        Log.v(TAG, "running on the thread : ${Thread.currentThread().name}")
    }

    /**
     * The error function.
     * Pops up a Toast if there's no internet connection
     * or if it couldn't get results.
     */
    open fun errorFunction(exception: Exception) {
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