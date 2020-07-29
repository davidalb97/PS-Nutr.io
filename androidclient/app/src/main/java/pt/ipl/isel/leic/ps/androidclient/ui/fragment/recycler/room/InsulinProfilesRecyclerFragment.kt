package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.InsulinProfileRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.JWT
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel

class InsulinProfilesRecyclerFragment :
    ARoomRecyclerListFragment<InsulinProfile, InsulinProfilesRecyclerViewModel>() {

    private val adapter: InsulinProfileRecyclerAdapter by lazy {
        InsulinProfileRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = InsulinProfilesVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[InsulinProfilesRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.insulin_profiles_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jwt = sharedPreferences.getString(JWT, "")

        viewModel.refreshLayout =
            view.findViewById(R.id.insulin_refresh_layout)

        if (jwt != null) {
            viewModel.jwt = jwt
        }

        viewModel.onError = {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }

        noItemsLabel =
            view.findViewById(R.id.no_insulin_profiles)
        initRecyclerList(view)
        setErrorFunction()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        viewModel.update()

        startObserver()


        val addButton =
            view.findViewById<ImageButton>(R.id.add_profile)

        // Setups a listener to go to the fragment that adds a profile
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.nav_add_insulin)
        }

        // Setups a listener that refresh the displayed information by swiping down
        viewModel.refreshLayout.setOnRefreshListener(object :
            SwipeRefreshLayout(this.requireContext()),
            SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.update()
            }
        })
    }

    override fun startScrollListener() {
        TODO("Not yet implemented")
    }

    override fun successFunction(list: List<InsulinProfile>) {
        super.successFunction(list)
        progressWheel.visibility = View.INVISIBLE
        noItemsLabel.visibility = if (list.isEmpty()) View.VISIBLE else View.INVISIBLE
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar
}