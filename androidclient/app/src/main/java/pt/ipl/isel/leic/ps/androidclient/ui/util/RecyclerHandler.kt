package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.os.Parcelable
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.BaseRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel

class RecyclerHandler<M : Parcelable, VM : BaseListViewModel<M>, A : BaseRecyclerAdapter<*, *, *>>(
    @IdRes val recyclerId: Int,
    @IdRes val noItemsTextViewId: Int,
    @IdRes val progressBarId: Int,
    val recyclerAdapter: A,
    val recyclerViewModel: VM,
    val view: View,
    val paginated: Boolean,
    val onError: (Throwable) -> Unit
) {

    private val recyclerView: RecyclerView = view.findViewById(recyclerId)
    private val recyclerNoItemsTxtView: TextView = view.findViewById(noItemsTextViewId)
    private val recyclerProgressWheel: ProgressBar = view.findViewById(progressBarId)

    init {

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerProgressWheel.visibility = View.VISIBLE

        //TODO Check if list.setHasFixedSize(true) is required
        //list.setHasFixedSize(true)

        recyclerViewModel.onError = this::errorFunction

        if (paginated) {
            startScrollListener()
        }
    }

    /**
     * Starts the observer inside the view model
     */
    fun startObserver(owner: LifecycleOwner) {
        onRecyclerFetchItems()
        recyclerViewModel.observe(owner) {
            recyclerView.adapter?.notifyDataSetChanged()
            successFunction(it)
        }
    }

    /**
     * Recycler list scroll listener
     * Receives the fetch from the specific view model
     */
    private fun startScrollListener() {
        recyclerView.addOnScrollListener(
            AppScrollListener(
                layoutManager = recyclerView.layoutManager as LinearLayoutManager,
                recyclerViewModel = recyclerViewModel
            )
        )
    }

    /**
     * The success function.
     * A Toast will pop up telling no results were found, if a request
     * returns an empty list.
     */
    fun successFunction(list: List<M>) {
        if (list.isEmpty()) {
            onNoRecyclerItems()
        } else onRecyclerItems()
        recyclerViewModel.pendingRequest = false
    }

    /**
     * The error function.
     * Pops up a Toast if there's no internet connection
     * or if it couldn't get results.
     */
    fun errorFunction(exception: Throwable) {
        onError(exception)
        onNoRecyclerItems()
        recyclerViewModel.pendingRequest = false
    }

    fun onRecyclerFetchItems() {
        this.recyclerView.visibility = View.VISIBLE
        this.recyclerProgressWheel.visibility = View.VISIBLE
        this.recyclerNoItemsTxtView.visibility = View.GONE
    }

    fun onRecyclerItems() {
        this.recyclerView.visibility = View.VISIBLE
        this.recyclerProgressWheel.visibility = View.GONE
        this.recyclerNoItemsTxtView.visibility = View.GONE
    }

    fun onNoRecyclerItems() {
        this.recyclerView.visibility = View.GONE
        this.recyclerProgressWheel.visibility = View.GONE
        this.recyclerNoItemsTxtView.visibility = View.VISIBLE
    }
}