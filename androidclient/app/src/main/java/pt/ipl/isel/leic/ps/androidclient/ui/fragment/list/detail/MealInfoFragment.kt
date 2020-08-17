package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealInfoRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.BaseListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IImage
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.ICalculatorActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IFavoriteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IVoteActionButtons
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.AMenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IEditMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IPopupMenuButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MealInfoVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.MealInfoViewModel

class MealInfoFragment :
    BaseListFragment<MealInfo, MealInfoViewModel, MealInfoRecyclerAdapter>(),
    IVoteActionButtons,
    ICalculatorActionButton,
    IImage,
    IFavoriteActionButton,
    IPopupMenuButton,
    IReportMenuItem,
    IEditMenuItem {

    override val recyclerAdapter by lazy {
        MealInfoRecyclerAdapter(
            recyclerViewModel,
            this.requireContext()
        )
    }
    override val menus: MutableList<AMenuItemFactory> = mutableListOf()
    override lateinit var actions: List<ItemAction>
    override lateinit var image: ImageView
    override lateinit var voteCountersLayout: ViewGroup
    override lateinit var votesBar: ProgressBar
    override lateinit var voteButtonsLayout: ViewGroup
    override lateinit var upVoteCounter: TextView
    override lateinit var downVoteCounter: TextView
    override lateinit var favoriteButton: ImageButton
    override lateinit var calculatorButton: ImageButton
    override lateinit var upVoteButton: ImageButton
    override lateinit var downVoteButton: ImageButton
    override lateinit var menuButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actions = recyclerViewModel.actions
        recyclerViewModel.observeInfo(this) { mealInfo ->
            setupView(view, mealInfo)
        }
        //Fetch meal info
        recyclerViewModel.update()
    }

    private fun setupView(view: View, receivedMeal: MealInfo) {
        image = view.findViewById(R.id.meal_detail_image)
        super.setupImage(view, receivedMeal.imageUri)

        if (receivedMeal.restaurantSubmissionId != null) {
            voteCountersLayout = view.findViewById(R.id.vote_counters_layout)
            votesBar = view.findViewById(R.id.votesBar)
            upVoteCounter = view.findViewById(R.id.upVoteCounter)
            downVoteCounter = view.findViewById(R.id.downVoteCounter)
            super.setupVoteBarCounters(receivedMeal.votes)

            voteButtonsLayout = view.findViewById(R.id.vote_buttons_layout)
            downVoteButton = view.findViewById(R.id.down_vote_button)
            upVoteButton = view.findViewById(R.id.up_vote_button)
            super.setupVoteButtons()
        }

        favoriteButton = view.findViewById(R.id.favorite)
        super.setupFavoriteButton()

        calculatorButton = view.findViewById(R.id.add_to_calc_action)
        super.setupCalculateAction(view)

        menuButton = view.findViewById(R.id.options)
        super.setupReportMenuItem()
        super.setupEditMenuItem()
        super.setupPopupMenuButton()

        val title: TextView = view.findViewById(R.id.meal_detail_title)
        title.text = receivedMeal.name

        if (receivedMeal.source != Source.CUSTOM && receivedMeal.isSuggested) {
            val suggestedLayout: RelativeLayout = view.findViewById(R.id.meal_info_suggested_rl)
            suggestedLayout.visibility = View.VISIBLE
        }
    }

    override fun onEdit(onSuccess: () -> Unit) {
        sendToDestination(requireView(), Navigation.SEND_TO_ADD_CUSTOM_MEAL)
    }

    override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        recyclerViewModel.putFavorite(
            mealItem = recyclerViewModel.mealInfo!!,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onReport(reportStr: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        recyclerViewModel.report(
            mealItem = recyclerViewModel.mealInfo!!,
            reportStr = reportStr,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onVote(
        voteState: VoteState,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        recyclerViewModel.setVote(
            vote = voteState,
            userSession = requireUserSession(),
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putMealInfo(recyclerViewModel.mealInfo)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)    //Edit meal / calculator back
    }

    override fun fetchVotes(): Votes? = recyclerViewModel.mealInfo?.votes

    override fun fetchCtx(): Context = requireContext()

    override fun isFavorite(): Boolean = recyclerViewModel.mealInfo!!.isFavorite

    override fun getRecyclerId() = R.id.meal_info_ingredient_item_list

    override fun getProgressBarId() = R.id.meal_info_progress_bar

    override fun getNoItemsLabelId() = R.id.meal_info_no_ingredients

    override fun getLayout() = R.layout.meal_detail

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): AViewModelProviderFactory {
        return MealInfoVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getRecyclerViewModelClass() = MealInfoViewModel::class.java
}