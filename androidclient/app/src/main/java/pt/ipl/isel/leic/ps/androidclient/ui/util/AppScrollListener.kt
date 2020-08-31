package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel

/**
 * For pagination proposes.
 * @param   layoutManager   The recycler view layout manager
 * @param   recyclerViewModel   The view model where this listener is being used
 */
class AppScrollListener<M : Parcelable>(
    private val layoutManager: LinearLayoutManager,
    private val recyclerViewModel: BaseListViewModel<M>
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && shouldGetMore()) {
            if (visibleItems + firstVisibleItem >= totalItems / 2 && firstVisibleItem >= 0) {
                loadMore()
            }
        }
    }

    private fun isLoading(): Boolean = recyclerViewModel.pendingRequest

    private fun loadMore() {
        recyclerViewModel.currItems = recyclerViewModel.items.size + 1
        recyclerViewModel.skip++
        recyclerViewModel.update()
    }

    private fun shouldGetMore(): Boolean =
        recyclerViewModel.currItems < recyclerViewModel.items.size
}