package pt.ipl.isel.leic.ps.androidclient.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.MealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.ARequestRecyclerListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.RestaurantInfoVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantInfoMealRecyclerViewModel

class RestaurantDetailFragment : MealRecyclerFragment(){

    private val logger = Logger(RestaurantDetailFragment::class)
    private lateinit var innerViewModel: RestaurantInfoMealRecyclerViewModel

    /**
     * ViewModel builder
     * Initializes the view model, calling the respective
     * view model provider factory
     */
    protected override fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = RestaurantInfoVMProviderFactory(savedInstanceState, rootActivity.intent)
        innerViewModel = ViewModelProvider(rootActivity, factory)[RestaurantInfoMealRecyclerViewModel::class.java]
        viewModel = innerViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.restaurant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO handle error
        innerViewModel.fetchInfo(::setupRestaurantInfoView, logger::e)
    }

    private fun setupRestaurantInfoView(restaurantInfo: RestaurantInfo) {
        //TODO fill data on restaurant info
    }

    override fun getRecyclerId() = R.id.restaurant_meals_list

    override fun getProgressBarId() = R.id.restaurant_meals_progress_bar
}