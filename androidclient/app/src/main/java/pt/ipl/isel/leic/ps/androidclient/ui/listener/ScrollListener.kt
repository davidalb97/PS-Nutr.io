package pt.ipl.isel.leic.ps.androidclient.ui.listener

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A listener for Recycler Views' scroll control.
 */
abstract class ScrollListener
    (var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    var isLoading = false
    lateinit var progressBar: View

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading && shouldGetMore()) {
            if (visibleItems + firstVisibleItem >= totalItems && firstVisibleItem >= 0) {
                loadMore()
            }
        }
    }

    abstract fun loadMore()

    abstract fun shouldGetMore(): Boolean

    fun startLoading() {
        isLoading = true
        progressBar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        isLoading = false
        progressBar.visibility = View.INVISIBLE
    }
}