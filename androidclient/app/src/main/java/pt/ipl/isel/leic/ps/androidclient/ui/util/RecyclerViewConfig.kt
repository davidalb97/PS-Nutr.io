package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R
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
    listId: Int
) {

    // Get RecyclerView
    val list =
        owner.activity?.findViewById(listId) as RecyclerView

    list.setHasFixedSize(true)
    list.layoutManager = LinearLayoutManager(owner.requireContext())

    // Define adapter
    list.adapter = adapter

    viewModel.liveData?.observe(owner, Observer {
        /*viewModel.adapter = ListAdapter(viewModel)
        list.adapter = viewModel.adapter*/
        adapter.notifyDataSetChanged()


    })

    list.addOnScrollListener(object :
        ScrollListener(list.layoutManager as LinearLayoutManager) {

        var minimumListSize = 1
        init {
            //progressBar = owner.activity?.findViewById(R.id.progressBar)!!
        }

        override fun loadMore() {
            minimumListSize = viewModel.liveData?.value!!.size + 1
            startLoading()
            //viewModel.getItems()
            stopLoading()
        }

        override fun shouldGetMore(): Boolean =
            !isLoading && minimumListSize < viewModel.liveData?.value!!.size
    })
}


