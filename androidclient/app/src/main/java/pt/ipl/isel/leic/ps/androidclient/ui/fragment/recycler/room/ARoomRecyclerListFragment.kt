package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.ARecyclerListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

abstract class ARoomRecyclerListFragment<M : Any, VM : ARecyclerViewModel<M>>
    : ARecyclerListFragment<M, VM>() {

    override fun initRecyclerList(view: View) {
        this.list =
            view.findViewById(R.id.itemList) as RecyclerView

        list.setHasFixedSize(true)
    }

    override fun startObserver() {
        viewModel.observe(this) {
            list.adapter?.notifyDataSetChanged()
            if (viewModel.mediatorLiveData.value!!.isEmpty()) {
                val noProfilesText =
                    requireView().findViewById<TextView>(R.id.no_insulin_profiles)
                noProfilesText.visibility = View.VISIBLE
            }
        }
    }

    override fun successFunction(list: List<M>) {
        TODO("Not yet implemented")
    }

    override fun errorFunction(exception: Exception) {
        TODO("Not yet implemented")
    }


}