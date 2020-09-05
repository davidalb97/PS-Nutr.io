package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealItemRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.BaseListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IImage
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IFavoriteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IVoteActionButtons
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IEditMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IPopupMenuButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.MenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.RestaurantInfoViewModel

class RestaurantInfoFragment :
    BaseListFragment<MealItem, RestaurantInfoViewModel, MealItemRecyclerAdapter>(),
    IImage,
    IVoteActionButtons,
    IFavoriteActionButton,
    IPopupMenuButton,
    IReportMenuItem,
    IEditMenuItem,
    ISend {

    override val recyclerAdapter by lazy {
        MealItemRecyclerAdapter(
            recyclerViewModel,
            this.requireContext()
        )
    }
    override val menus: MutableMap<String, MenuItemFactory> = mutableMapOf()
    override lateinit var actions: List<ItemAction>
    override val imageId: Int = R.id.restaurant_detail_image
    override lateinit var image: ImageView
    override val voteCountersLayoutId: Int = R.id.vote_counters_layout
    override lateinit var voteCountersLayout: ViewGroup
    override val votesBarId: Int = R.id.votesBar
    override lateinit var votesBar: ProgressBar
    override val voteButtonsLayoutId: Int = R.id.vote_buttons_layout
    override lateinit var voteButtonsLayout: ViewGroup
    override val upVoteCounterId: Int = R.id.upVoteCounter
    override lateinit var upVoteCounter: TextView
    override val downVoteCounterId: Int = R.id.downVoteCounter
    override lateinit var downVoteCounter: TextView
    override val favoriteButtonId: Int = R.id.favorite
    override lateinit var favoriteButton: ImageButton
    override val upVoteButtonId: Int = R.id.up_vote_button
    override lateinit var upVoteButton: ImageButton
    override val downVoteButtonId: Int = R.id.down_vote_button
    override lateinit var downVoteButton: ImageButton
    override val menuButtonId: Int = R.id.options
    override lateinit var menuButton: ImageButton

    lateinit var addMealButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Bypass generic Recycler ViewModel creation
        val viewModel: RestaurantInfoViewModel
                by navGraphViewModels(Navigation.SEND_TO_RESTAURANT_DETAIL.navId)
        recyclerViewModel = viewModel
        recyclerViewModel.restaurantId = requireNotNull(arguments?.getRestaurantItem()?.id) {
            "Restaurant detail requires submission ID"
        }
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actions = recyclerViewModel.actions
        recyclerViewModel.observeInfo(this) { restaurantInfo ->
            setupRestaurantInfoView(view, restaurantInfo)
        }
        recyclerViewModel.update()
    }

    private fun setupRestaurantInfoView(view: View, restaurantInfo: RestaurantInfo) {
        super.setupImage(view, restaurantInfo.image)
        super.setupVoteBarCounters(view, restaurantInfo.votes)
        super.setupFavoriteButton(view, restaurantInfo.favorites)
        super.setupReportMenuItem(restaurantInfo.isReportable)
        super.setupEditMenuItem()
        super.setupPopupMenuButton(view)
        super.setupVoteButtons(view, restaurantInfo.votes)

        val title: TextView = view.findViewById(R.id.restaurant_detail_title)
        title.text = restaurantInfo.name

        addMealButton = view.findViewById(R.id.add_meal_button)
        addMealButton.setOnClickListener {
            ensureUserSession(requireContext()) {
                view.findNavController().navigate(Navigation.SEND_TO_PICK_RESTAURANT_MEAL.navId)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val addedMeal = recyclerViewModel.addedMeal
        if (addedMeal != null) {
            recyclerViewModel.liveDataHandler.add(addedMeal)
            recyclerViewModel.addedMeal = null
        }
    }

    override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        recyclerViewModel.favorite(
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onReport(reportStr: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        recyclerViewModel.report(
            reportMsg = reportStr,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onVote(voteState: VoteState, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        recyclerViewModel.vote(
            vote = voteState,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onEdit(onSuccess: () -> Unit) {
        sendToDestination(requireView(), Navigation.SEND_TO_ADD_RESTAURANT)
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putRestaurantInfo(recyclerViewModel.restaurantInfo)  //Edit restaurant
        bundle.putNavigation(Navigation.SEND_TO_RESTAURANT_DETAIL)  //Edit restaurant back
    }

    override fun fetchCtx(): Context = requireContext()

    override fun getRecyclerId() = R.id.restaurant_meals_list

    override fun getProgressBarId() = R.id.restaurant_meals_progress_bar

    override fun getLayout() = R.layout.restaurant_detail

    override fun getNoItemsLabelId() = R.id.restaurant_info_no_meals_found

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): BaseViewModelProviderFactory = throw UnsupportedOperationException("Not used")

    override fun getRecyclerViewModelClass() = RestaurantInfoViewModel::class.java
}