package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.PortionOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealItemRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.BaseListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IChart
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IImage
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.ICalculatorActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IFavoriteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IVoteActionButtons
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IEditMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IPopupMenuButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.MenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MealInfoVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.MealInfoViewModel

class MealInfoFragment :
    BaseListFragment<MealItem, MealInfoViewModel, MealItemRecyclerAdapter>(),
    IVoteActionButtons,
    ICalculatorActionButton,
    IImage,
    IChart,
    ISend,
    IFavoriteActionButton,
    IPopupMenuButton,
    IReportMenuItem,
    IEditMenuItem {

    override val recyclerAdapter by lazy {
        MealItemRecyclerAdapter(
            recyclerViewModel,
            this.requireContext()
        )
    }
    override val menus: MutableMap<String, MenuItemFactory> = mutableMapOf()
    override lateinit var actions: List<ItemAction>
    override val imageId: Int = R.id.meal_detail_image
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
    override val calculatorButtonId: Int = R.id.add_to_calc_action
    override lateinit var calculatorButton: ImageButton
    override val upVoteButtonId: Int = R.id.up_vote_button
    override lateinit var upVoteButton: ImageButton
    override val downVoteButtonId: Int = R.id.down_vote_button
    override lateinit var downVoteButton: ImageButton
    override val menuButtonId: Int = R.id.options
    override lateinit var menuButton: ImageButton
    override val chartId: Int = R.id.portion_chart
    override lateinit var chart: BarChart

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
        super.setupImage(view, receivedMeal.imageUri)

        if (receivedMeal.restaurantSubmissionId != null) {
            super.setupVoteBarCounters(view, receivedMeal.votes, receivedMeal.votes?.isVotable ?: false)
            super.setupVoteButtons(view, receivedMeal.votes?.isVotable ?: false)
        }

        super.setupFavoriteButton(view)
        super.setupCalculateAction(view)
        super.setupReportMenuItem()
        super.setupEditMenuItem()
        super.setupPopupMenuButton(view)

        val amountMap: HashMap<Float, Int> = hashMapOf()
        val portionEntries =
            receivedMeal.portions.map { portion ->
                val amount = portion.amount
                amountMap[amount]?.inc() ?: amountMap.put(amount, 1)
                BarEntry(portion.amount, amountMap[amount]!!.toFloat())
            }
        super.setupChart(view, portionEntries)

        val addPortionButton = view.findViewById<ImageButton>(R.id.add_portion_button)
        addPortionButton.setOnClickListener {
            MealAmountSelector(
                ctx = requireContext(),
                layoutInflater = layoutInflater,
                baseCarbs = 0f,
                baseAmountGrams = 0f,
                mealUnit = WeightUnits.fromValue(sharedPreferences.getWeightUnitOrDefault())
            ) { preciseGrams, preciseCarbs ->
                recyclerViewModel.addMealPortion(
                    restaurantId = receivedMeal.restaurantSubmissionId,
                    mealId = receivedMeal.submissionId,
                    portionOutput = PortionOutput(
                        preciseGrams.toInt(),
                        sharedPreferences.getWeightUnitOrDefault()
                    ),
                    userSession = requireUserSession(),
                    onSuccess = { status ->
                        Toast.makeText(app, status, Toast.LENGTH_SHORT).show()
                    },
                    onError = { error ->
                        Toast.makeText(app, "Could not add portion", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        val title: TextView = view.findViewById(R.id.meal_detail_title)
        title.text = receivedMeal.name

        if (receivedMeal.source != Source.CUSTOM && receivedMeal.isSuggested == true) {
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

    override fun isFavorite(): Boolean = recyclerViewModel.mealInfo!!.favorites?.isFavorite ?: false

    override fun getRecyclerId() = R.id.meal_info_ingredient_item_list

    override fun getProgressBarId() = R.id.meal_info_progress_bar

    override fun getNoItemsLabelId() = R.id.meal_info_no_ingredients

    override fun getLayout() = R.layout.meal_detail

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): BaseViewModelProviderFactory {
        return MealInfoVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getRecyclerViewModelClass() = MealInfoViewModel::class.java
}