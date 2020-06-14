package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.ARecyclerListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

abstract class ARoomRecyclerListFragment<M : Any, VM : ARecyclerViewModel<M>>
    : ARecyclerListFragment<M, VM>() {

    lateinit var noItemsLabel: TextView
    lateinit var progressWheel: ProgressBar

    override fun initRecyclerList(view: View) {
        this.list = view.findViewById(getRecyclerId())

        this.progressWheel = view.findViewById(getProgressBarId())

        this.progressWheel.visibility = View.VISIBLE

        list.setHasFixedSize(true)
    }
}