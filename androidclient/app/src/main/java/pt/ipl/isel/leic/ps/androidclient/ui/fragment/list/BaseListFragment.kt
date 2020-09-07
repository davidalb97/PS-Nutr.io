package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.BaseRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseViewModelFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.RecyclerHandler
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel

abstract class BaseListFragment<
        M : Parcelable,
        VM : BaseListViewModel<M>,
        A : BaseRecyclerAdapter<*, *, *>
        > : BaseViewModelFragment<VM>() {

    protected abstract val paginated: Boolean
    protected abstract val recyclerViewId: Int
    protected abstract val progressBarId: Int
    protected abstract val noItemsTextViewId: Int
    abstract val recyclerAdapter: A
    protected lateinit var recyclerHandler: RecyclerHandler<M, VM, A>

    /**
     * Gets the list elements and initializes them
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View) {
        log.v("Initializing recycler handler...")
        recyclerHandler = RecyclerHandler(
            recyclerId = recyclerViewId,
            noItemsTextViewId = noItemsTextViewId,
            progressBarId = progressBarId,
            recyclerAdapter = recyclerAdapter,
            recyclerViewModel = viewModel,
            view = view,
            paginated = paginated,
            onError = ::onError
        )
        recyclerHandler.startObserver(this)
    }
}