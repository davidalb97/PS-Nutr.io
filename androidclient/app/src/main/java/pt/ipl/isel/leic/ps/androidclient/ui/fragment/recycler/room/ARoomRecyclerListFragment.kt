package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.ARecyclerListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.ARecyclerViewModel

abstract class ARoomRecyclerListFragment<M : Any, VM : ARecyclerViewModel<M>>
    : ARecyclerListFragment<M, VM>() {

    lateinit var noProfilesTextView: TextView

    override fun initRecyclerList(view: View) {
        this.list =
            view.findViewById(R.id.itemList) as RecyclerView

        this.noProfilesTextView =
            view.findViewById(R.id.no_insulin_profiles)
        list.setHasFixedSize(true)
    }

    override fun startObserver() {
        viewModel.observe(this) {
            list.adapter?.notifyDataSetChanged()
            if (viewModel.mediatorLiveData.value!!.isEmpty()) {
                noProfilesTextView.visibility = View.VISIBLE
            } else {
                noProfilesTextView.visibility = View.INVISIBLE
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