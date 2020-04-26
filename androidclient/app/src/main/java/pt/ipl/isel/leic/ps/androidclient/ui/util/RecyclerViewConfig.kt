package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun <T> configureRecyclerListView(
    ctx: Fragment,
    viewModel: AViewModel<T>,
    listId: Int,
    progressBarId: Int = 0
) {

    // Get RecyclerView
    val list =
        ctx.activity?.findViewById(listId) as RecyclerView

    // Get ProgressBar if needed
    val progressBar =
        if (progressBarId == 0)
            null
        else
            ctx.activity?.findViewById(progressBarId) as ProgressBar

    list.setHasFixedSize(true)
    list.layoutManager = LinearLayoutManager(ctx.requireContext())

    // Define adapter
    list.adapter =
        ListAdapter(viewModel)

    viewModel.liveInfo.observe(ctx, Observer {
        viewModel.adapter = ListAdapter(viewModel)
        list.adapter = viewModel.adapter

        if (progressBar != null) {
            progressBar.visibility = if (viewModel.liveInfo.value!!.requestPending)
                View.VISIBLE else View.INVISIBLE
        }

    })

    list.addOnScrollListener(object :
        ScrollListener(list.layoutManager as LinearLayoutManager) {

        var minimumListSize = 1

        override fun isLoading() = viewModel.liveInfo.value!!.requestPending


        override fun loadMore() {
            minimumListSize = viewModel.liveInfo.value!!.list.size + 1
            viewModel.getMoreItems()
        }

        override fun shouldGetMore(): Boolean =
            minimumListSize < viewModel.liveInfo.value!!.list.size
    })
}


