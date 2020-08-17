package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.InsulinProfileRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel

class InsulinProfilesListFragment : BaseListFragment<
        InsulinProfile,
        InsulinProfilesListViewModel,
        InsulinProfileRecyclerAdapter
        >() {

    var refreshLayout: SwipeRefreshLayout? = null

    override val recyclerAdapter: InsulinProfileRecyclerAdapter by lazy {
        InsulinProfileRecyclerAdapter(
            recyclerViewModel,
            this.requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshLayout = view.findViewById(R.id.insulin_refresh_layout)

        val addButton = view.findViewById<ImageButton>(R.id.add_profile)

        // Setups a listener to go to the fragment that adds a profile
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.nav_add_insulin)
        }

        // Setups a listener that refresh the displayed information by swiping down
        refreshLayout!!.setOnRefreshListener(object :
            SwipeRefreshLayout(this.requireContext()),
            SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                recyclerViewModel.update()
            }
        })
        recyclerViewModel.update()
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar

    override fun getLayout() = R.layout.insulin_profiles_fragment

    override fun getNoItemsLabelId() = R.id.no_insulin_profiles

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): InsulinProfilesVMProviderFactory {
        return InsulinProfilesVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getRecyclerViewModelClass() = InsulinProfilesListViewModel::class.java

}