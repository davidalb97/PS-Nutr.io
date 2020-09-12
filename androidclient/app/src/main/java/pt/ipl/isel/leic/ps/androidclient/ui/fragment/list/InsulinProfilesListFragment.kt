package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.InsulinProfileRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putParentNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel

class InsulinProfilesListFragment : BaseListFragment<
        InsulinProfile,
        InsulinProfilesListViewModel,
        InsulinProfileRecyclerAdapter
        >() {

    override val paginated = false
    override val recyclerViewId = R.id.itemList
    override val progressBarId = R.id.progressBar
    override val layout = R.layout.insulin_profiles_fragment
    override val noItemsTextViewId = R.id.no_insulin_profiles

    override val vmClass = InsulinProfilesListViewModel::class.java
    override val vMProviderFactorySupplier = ::InsulinProfilesVMProviderFactory
    override val viewModel: InsulinProfilesListViewModel
            by navGraphViewModels(Navigation.SEND_TO_INSULIN_PROFILES.navId) {
                vMProviderFactorySupplier(arguments, savedInstanceState, requireIntent())
            }
    override val recyclerAdapter: InsulinProfileRecyclerAdapter by lazy {
        InsulinProfileRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private var refreshLayout: SwipeRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Init super's recycler list handler
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById(R.id.insulin_refresh_layout)

        if(viewModel.actions.contains(ItemAction.ADD)) {
            val addButton = view.findViewById<ImageButton>(R.id.add_profile)

            // Setups a listener to go to the fragment that adds a profile
            addButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putNavigation(Navigation.BACK_TO_INSULIN_PROFILES_FROM_ADD_PROFILE)
                bundle.putParentNavigation(Navigation.SEND_TO_INSULIN_PROFILES)
                super.navigate(Navigation.SEND_TO_ADD_INSULIN_PROFILE_FROM_PROFILES, bundle)
            }
        }

        // Setups a listener that refresh the displayed information by swiping down
        refreshLayout!!.setOnRefreshListener { //TODO Should clear and then fetch
            viewModel.triggerFetch()
        }
        viewModel.observe(this) {
            refreshLayout!!.isRefreshing = false
        }
        viewModel.setupList()
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.itemsChanged) {
            viewModel.itemsChanged = false
            recyclerAdapter.notifyDataSetChanged()
        }
    }
}