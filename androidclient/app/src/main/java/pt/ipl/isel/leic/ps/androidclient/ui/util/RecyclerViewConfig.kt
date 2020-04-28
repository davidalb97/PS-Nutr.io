package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.ListAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.listener.ScrollListener
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AViewModel

/**
 * Setups a generic RecyclerList
 * (must be called inside the activity / fragment)
 */
fun <T> configureRecyclerList(
    owner: Fragment,
    viewModel: AViewModel<T>,
    adapter: ListAdapter<T>,
    listId: Int,
    progressBarId: Int = 0
) {

    // Get RecyclerView
    val list =
        owner.activity?.findViewById(listId) as RecyclerView

    // Get ProgressBar if needed
    val progressBar =
        if (progressBarId == 0)
            null
        else
            owner.activity?.findViewById(progressBarId) as ProgressBar

    list.setHasFixedSize(true)
    list.layoutManager = LinearLayoutManager(owner.requireContext())

    // Define adapter
    list.adapter = adapter

    viewModel.liveData?.observe(owner, Observer {
        /*viewModel.adapter = ListAdapter(viewModel)
        list.adapter = viewModel.adapter*/
        adapter.notifyDataSetChanged()
        /*if (progressBar != null) {
            progressBar.visibility =
                if (viewModel.liveData.value!!.requestPending)
                    View.VISIBLE
                else
                    View.INVISIBLE
        }*/

    })

    list.addOnScrollListener(object :
        ScrollListener(list.layoutManager as LinearLayoutManager) {

        var minimumListSize = 1

        override fun isLoading() = false//viewModel.liveData.value!!.requestPending


        override fun loadMore() {
            minimumListSize = viewModel.liveData?.value!!.size + 1
            viewModel.getMoreItems()
        }

        override fun shouldGetMore(): Boolean =
            minimumListSize < viewModel.liveData?.value!!.size
    })
}


