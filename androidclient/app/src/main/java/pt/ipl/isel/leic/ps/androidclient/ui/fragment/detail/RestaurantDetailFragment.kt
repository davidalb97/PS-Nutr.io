package pt.ipl.isel.leic.ps.androidclient.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.restaurantRepository
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_RESTAURANT_INFO_ID
import pt.ipl.isel.leic.ps.androidclient.ui.provider.RestaurantInfoVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantInfoMealRecyclerViewModel

class RestaurantDetailFragment : MealRecyclerFragment(){

    private val log = Logger(RestaurantDetailFragment::class)
    private lateinit var innerViewModel: RestaurantInfoMealRecyclerViewModel


    /**
     * ViewModel builder
     * Initializes the view model, calling the respective
     * view model provider factory
     */
    override fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = RestaurantInfoVMProviderFactory(savedInstanceState, rootActivity.intent, requireArguments())
        innerViewModel = ViewModelProvider(rootActivity, factory)[RestaurantInfoMealRecyclerViewModel::class.java]
        innerViewModel.restaurantId = requireArguments().getString(BUNDLE_RESTAURANT_INFO_ID)!!
        viewModel = innerViewModel
        activityApp = requireActivity().application
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

        innerViewModel.fetchInfo(::setupRestaurantInfoView, log::e)
    }

    private fun setupRestaurantInfoView(restaurantInfo: RestaurantInfo) {
        val restaurantMealImage =
            view?.findViewById<ImageView>(R.id.restaurant_detail_image)
        val restaurantMealTitle =
            view?.findViewById<TextView>(R.id.restaurant_detail_title)
        val restaurantUpvoteButton =
            view?.findViewById<Button>(R.id.upvote)
        val restaurantDownvoteButton =
            view?.findViewById<Button>(R.id.downvote)
        val restaurantAddMealButton =
            view?.findViewById<Button>(R.id.upvote)

        // Image loading
        if (restaurantInfo.imageUri == null)
            restaurantMealImage?.visibility = View.GONE
        else
            Glide.with(requireView())
                .load(restaurantInfo.imageUri)
                .into(restaurantMealImage!!)

        restaurantMealTitle?.text = restaurantInfo.name

        // Upvote
        restaurantUpvoteButton?.setOnClickListener { view ->
            restaurantRepository.putVote(restaurantInfo.id, true, {
                //TODO change vote color
            }, log::e)
        }

        // Downvote
        restaurantDownvoteButton?.setOnClickListener { view ->
            restaurantRepository.putVote(restaurantInfo.id, false, {
                //TODO change vote color
            },log::e)
        }
    }

    override fun getRecyclerId() = R.id.restaurant_meals_list

    override fun getProgressBarId() = R.id.restaurant_meals_progress_bar
}