package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.ARecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.listener.ScrollListener
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.ARecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CuisineRecyclerViewModel

// Default pagination value
const val COUNT = 10

abstract class RecyclerListFragment<T : Any> : Fragment() {

    lateinit var viewModel: ARecyclerViewModel<T>
    lateinit var adapter: ARecyclerAdapter<T, ARecyclerViewModel<T>, ARecyclerViewHolder<T>>

    lateinit var list: RecyclerView
    lateinit var progressBar: ProgressBar

    /**
     * Gets the list elements and initializes them
     */
    fun initRecyclerList(view: View) {
        this.list =
            view.findViewById(R.id.itemList) as RecyclerView

        // List progressBar
        this.progressBar =
            view.findViewById(R.id.progressBar) as ProgressBar

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(this.requireContext())
    }

    /**
     * Starts the observer inside the view model
     */
    fun startObserver() {
        viewModel.observe(this) {
            adapter.notifyDataSetChanged()

            if (viewModel.mediatorLiveData.value!!.isEmpty()) {
                Toast.makeText(
                    this.requireContext(),
                    R.string.no_result_found,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Recycler list scroll listener
     * Receives the fetch from the specific view model
     */
    fun startScrollListener() {
        list.addOnScrollListener(object :
            ScrollListener(list.layoutManager as LinearLayoutManager) {

            var minimumListSize = 1

            override fun loadMore() {
                minimumListSize = viewModel.liveData?.value!!.size + 1
                startLoading()
                viewModel.getMoreItemsExchangingLiveData()
                stopLoading()
            }

            override fun shouldGetMore(): Boolean =
                !isLoading && minimumListSize < viewModel.liveData?.value!!.size
        })
    }

    /**
     * The success function.
     * A Toast will pop up telling no results were found, if a request
     * returns an empty list.
     */
    open fun successFunction(): (List<Cuisine>) -> Unit = {
        if (it.isEmpty())
            Toast.makeText(
                requireActivity().application, R.string.no_result_found,
                Toast.LENGTH_LONG
            ).show()
        Log.v(TAG, "running on the thread : ${Thread.currentThread().name}")
    }

    /**
     * The error function.
     * Pops up a Toast if there's no internet connection
     * or if it couldn't get results.
     */
    fun errorFunction(): () -> Unit = {
        if (!hasInternetConnection(requireActivity().application as NutrioApp)) {
            Toast.makeText(
                requireActivity().application, R.string.no_internet_connection,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(requireActivity().application, R.string.error_network, Toast.LENGTH_LONG)
                .show()
        }
    }

}