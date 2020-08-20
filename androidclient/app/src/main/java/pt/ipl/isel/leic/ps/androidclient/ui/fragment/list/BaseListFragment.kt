package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.BaseRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.RecyclerHandler
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.BaseListViewModel

abstract class BaseListFragment<
        M : Parcelable,
        VM : BaseListViewModel<M>,
        A : BaseRecyclerAdapter<*, *, *>
        > : BaseFragment() {

    protected lateinit var recyclerViewModel: VM
    private lateinit var recyclerHandler: RecyclerHandler<M, VM, A>
    protected abstract val recyclerAdapter: A

    @IdRes
    protected abstract fun getRecyclerId(): Int

    @IdRes
    protected abstract fun getProgressBarId(): Int

    @IdRes
    protected abstract fun getNoItemsLabelId(): Int
    protected abstract fun getRecyclerViewModelClass(): Class<VM>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerViewModel = buildViewModel(savedInstanceState, getRecyclerViewModelClass())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Gets the list elements and initializes them
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View) {
        recyclerHandler = RecyclerHandler(
            recyclerId = getRecyclerId(),
            noItemsTxt = getNoItemsLabelId(),
            progressBar = getProgressBarId(),
            adapter = recyclerAdapter,
            recyclerViewModel = recyclerViewModel,
            view = view,
            onError = ::onError
        )
        recyclerHandler.startObserver(this)
    }
}