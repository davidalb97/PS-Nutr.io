package pt.ipl.isel.leic.ps.androidclient.ui.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class ScrollListener
    (var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    abstract fun isLoading() : Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && shouldGetMore()) {
            if (visibleItems + firstVisibleItem >= totalItems && firstVisibleItem >= 0) {
                loadMore()
            }
        }
    }

    abstract fun loadMore()

    abstract fun shouldGetMore(): Boolean
}