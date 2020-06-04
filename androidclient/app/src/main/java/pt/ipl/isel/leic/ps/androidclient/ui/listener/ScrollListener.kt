package pt.ipl.isel.leic.ps.androidclient.ui.listener

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A listener for Recycler Views' scroll control.
 */
abstract class ScrollListener(
    private val layoutManager: LinearLayoutManager,
    var progressBar: ProgressBar
) : RecyclerView.OnScrollListener() {

    var isLoading = false

    /**
     * The scroll routine
     */
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

    /**
     * Load more items
     */
    abstract fun loadMore()

    /**
     * Does it has the conditions to load more?
     */
    abstract fun shouldGetMore(): Boolean

    /**
     * Change to loading state
     */
    fun startLoading() {
        if (isLoading || progressBar.visibility == View.VISIBLE)
            return
        isLoading = true
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Stop loading state
     */
    fun stopLoading() {
        if (!isLoading || progressBar.visibility == View.INVISIBLE)
            return
        isLoading = false
        progressBar.visibility = View.INVISIBLE
    }
}