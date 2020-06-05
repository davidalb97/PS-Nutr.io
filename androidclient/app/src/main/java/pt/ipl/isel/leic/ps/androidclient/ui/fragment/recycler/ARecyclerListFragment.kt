package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

// Default pagination value
const val COUNT = 10

abstract class ARecyclerListFragment<T : Any, VM : ARecyclerViewModel<T>> : Fragment() {

    lateinit var viewModel: VM
    lateinit var list: RecyclerView
    lateinit var activityApp: Application

    /**
     * Gets the list elements and initializes them
     */
    abstract fun initRecyclerList(view: View)

    /**
     * Starts the observer inside the view model
     */
    fun startObserver() {
        viewModel.observe(this) {
            list.adapter?.notifyDataSetChanged()
            successFunction(it)
        }
    }

    /**
     * Recycler list scroll listener
     * Receives the fetch from the specific view model TODO
     */
    abstract fun startScrollListener()

    fun setErrorFunction() {
        viewModel.onError = this::errorFunction
    }

    /**
     * The success function.
     * A Toast will pop up telling no results were found, if a request
     * returns an empty list.
     */
    open fun successFunction(list: List<T>) {}

    /**
     * The error function.
     * Pops up a Toast if there's no internet connection
     * or if it couldn't get results.
     */
    open fun errorFunction(exception: Throwable) {}

}