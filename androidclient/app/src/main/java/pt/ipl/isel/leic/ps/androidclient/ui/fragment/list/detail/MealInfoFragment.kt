package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.detail

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealItemRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.BaseListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IBarChart
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IImage
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.ICalculatorActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IFavoriteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IVoteActionButtons
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IEditMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.MenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MealInfoVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.prompt.MealAmountSelector
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.MealInfoViewModel

class MealInfoFragment :
    BaseListFragment<MealItem, MealInfoViewModel, MealItemRecyclerAdapter>(),
    IVoteActionButtons,
    ICalculatorActionButton,
    IImage,
    IBarChart,
    ISend,
    IFavoriteActionButton,
    IReportMenuItem,
    IEditMenuItem {

    override val paginated = false
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
    override lateinit var calculatorButton: View
    override val upVoteButtonId: Int = R.id.up_vote_button
    override lateinit var upVoteButton: ImageButton
    override val downVoteButtonId: Int = R.id.down_vote_button
    override lateinit var downVoteButton: ImageButton
    override val chartId: Int = R.id.portion_chart
    override lateinit var chart: BarChart
    override lateinit var xAxisLabel: TextView
    override lateinit var yAxisLabel: TextView
    override var noDataText: String? = app.getString(R.string.no_portions_chart_message)

    override val recyclerViewId = R.id.meal_info_ingredient_item_list
    override val progressBarId = R.id.meal_info_progress_bar
    override val noItemsTextViewId = R.id.meal_info_no_ingredients
    override val layout = R.layout.meal_detail

    override val vmClass = MealInfoViewModel::class.java
    override val vMProviderFactorySupplier = ::MealInfoVMProviderFactory
    override val viewModel by lazy {
        buildViewModel(savedInstanceState, vmClass).also { viewModel ->
            actions = viewModel.actions
        }
    }
    override val recyclerAdapter by lazy {
        MealItemRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private val chartLayoutId: Int = R.id.chart_container
    private lateinit var chartLayout: ViewGroup
    private lateinit var addPortionLayout: RelativeLayout
    private lateinit var editPortionLayout: RelativeLayout
    private lateinit var portionEntries: MutableList<BarEntry>
    private var userPortionEntry: BarEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Init super's recycler list handler
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeInfo(this) { mealInfo ->
            setupView(view, mealInfo)
            viewModel.removeObservers(this)
        }

        //Fetch meal info
        viewModel.setupList()
    }

    private fun setupView(view: View, receivedMeal: MealInfo) {

        super.setupImage(view, receivedMeal.imageUri)
        super.setupVoteBarCounters(view, receivedMeal.votes)
        super.setupVoteButtons(view, receivedMeal.votes)
        super.setupFavoriteButton(view, receivedMeal.favorites)
        super.setupCalculateAction(view)
        super.setupReportMenuItem(receivedMeal.isReportable ?: false)
        super.setupEditMenuItem()
        populateMenu(NutrioApp.menu)

        //Only restaurant meals have portions
        if (receivedMeal.restaurantSubmissionId != null) {
            setupPortionEntries(receivedMeal)
            super.setupChart(view, portionEntries)
            setupPortionButtons(view, receivedMeal)
            chartLayout = view.findViewById(chartLayoutId)
            chartLayout.visibility = View.VISIBLE
        }

        val title: TextView = view.findViewById(R.id.meal_detail_title)
        title.text = receivedMeal.name

        if (receivedMeal.source != Source.CUSTOM_MEAL && receivedMeal.isSuggested == true) {
            val suggestedLayout: RelativeLayout = view.findViewById(R.id.meal_info_suggested_rl)
            suggestedLayout.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        NutrioApp.menu = menu
        populateMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupPortionEntries(receivedMeal: MealInfo) {
        val amountMap: HashMap<Float, Int> = hashMapOf()
        receivedMeal.portions?.allPortions?.forEach { amount ->
            amountMap[amount]?.inc() ?: amountMap.put(amount, 1)
        }
        portionEntries = amountMap.map {
            BarEntry(it.key, it.value.toFloat())
        }.toMutableList()

        val userPortionGrams = receivedMeal.portions?.userPortion
        if (userPortionGrams != null) {
            userPortionEntry = portionEntries.first { it.x == userPortionGrams }

            //If is first time (carbs from portion not obtained from server)
            if(viewModel.currentPortionCarbs == null) {
                //Set carbs relative to previous portion
                viewModel.currentPortionCarbs =
                    ((userPortionGrams * receivedMeal.carbs) / receivedMeal.amount)
            }
        }
    }

    private fun setupPortionButtons(view: View, receivedMeal: MealInfo) {
        addPortionLayout = view.findViewById(R.id.add_portion_layout)
        editPortionLayout = view.findViewById(R.id.edit_portion_layout)

        setupAddPortion(view, receivedMeal)
        setupEditPortion(view, receivedMeal)
        setupDeletePortion(view, receivedMeal)

        // If the current user has not any portion added to this meal
        if (receivedMeal.portions?.userPortion == null) {
            addPortionLayout.visibility = View.VISIBLE
        }
        // If the user already added a portion to this meal can now only edit or delete it
        else {
            editPortionLayout.visibility = View.VISIBLE
        }
    }

    private fun setupAddPortion(view: View, receivedMeal: MealInfo) {
        val addPortionButton: Button = view.findViewById(R.id.add_portion_button)
        addPortionButton.setOnClickListener {
            ensureUserSession(fetchCtx()) { userSession ->
                MealAmountSelector(
                    ctx = requireContext(),
                    layoutInflater = layoutInflater,
                    baseCarbs =  receivedMeal.carbs,
                    baseAmount = receivedMeal.unit.convert(viewModel.currentWeightUnit, receivedMeal.amount),
                    startUnit = viewModel.currentWeightUnit
                ) { portionAmount, portionCarbs, portionUnit ->
                    val amountGrams = portionUnit.convert(WeightUnits.GRAMS, portionAmount)
                    viewModel.addMealPortion(
                        restaurantId = receivedMeal.restaurantSubmissionId!!,
                        mealId = receivedMeal.submissionId!!,
                        portion = Portion(portionAmount, portionUnit),
                        userSession = userSession,
                        onSuccess = {
                            viewModel.currentPortionCarbs = portionCarbs
                            viewModel.currentWeightUnit = portionUnit
                            onAddPortion(amountGrams)
                        },
                        onError = { error -> onAddPortion(amountGrams, exception = error) }
                    )
                }
            }
        }
    }

    private fun setupEditPortion(view: View, receivedMeal: MealInfo) {
        val editPortionButton: Button = view.findViewById(R.id.edit_portion_button)
        editPortionButton.setOnClickListener {
            ensureUserSession(fetchCtx()) { userSession ->
                MealAmountSelector(
                    ctx = requireContext(),
                    layoutInflater = layoutInflater,
                    baseCarbs = viewModel.currentPortionCarbs!!,
                    baseAmount = WeightUnits.GRAMS.convert(
                        viewModel.currentWeightUnit,
                        userPortionEntry!!.x
                    ),
                    startUnit = viewModel.currentWeightUnit
                ) { portionAmount, portionCarbs, portionUnit ->
                    val amountGrams = portionUnit.convert(WeightUnits.GRAMS, portionAmount)
                    viewModel.editMealPortion(
                        restaurantId = receivedMeal.restaurantSubmissionId!!,
                        mealId = receivedMeal.submissionId!!,
                        portion = Portion(portionAmount, portionUnit),
                        userSession = userSession,
                        onSuccess = {
                            viewModel.currentPortionCarbs = portionCarbs
                            viewModel.currentWeightUnit = portionUnit
                            onEditPortion(amountGrams)
                        },
                        onError = { error -> onEditPortion(amountGrams, exception = error) }
                    )
                }
            }
        }
    }

    private fun setupDeletePortion(view: View, receivedMeal: MealInfo) {
        val deletePortionButton: Button = view.findViewById(R.id.delete_portion_button)
        deletePortionButton.setOnClickListener {
            ensureUserSession(fetchCtx()) { userSession ->
                viewModel.deleteMealPortion(
                    restaurantId = receivedMeal.restaurantSubmissionId!!,
                    mealId = receivedMeal.submissionId!!,
                    userSession = userSession,
                    onSuccess = {
                        viewModel.currentPortionCarbs = null
                        onDeletePortion()
                    },
                    onError = { error -> onDeletePortion(exception = error) }
                )
            }
        }
    }

    override fun setupChartSettings(entries: List<BarEntry>) {
        chart
            .setXAxisGranularity(10f)
            .setYAxisGranularity(1f)
            .setXAxisMin(0f)
            .setXAxisMax(entries.maxOf { it.x } + 10f)
            .setXDrawGridLines(false)
            .setYDrawGridLines(false)
            .setXAxisPosition(XAxis.XAxisPosition.BOTTOM)
            .setYAnimationTime(1000)
            .setChartDescription(false)
    }

    private fun onAddPortion(amountGrams: Float, exception: Exception? = null) {
        if (exception == null) {
            Toast.makeText(app, R.string.portion_added, Toast.LENGTH_SHORT).show()
            addPortionLayout.visibility = View.GONE
            editPortionLayout.visibility = View.VISIBLE

            addPortionToGraph(amountGrams)
            setupChartSettings(portionEntries)
            if (portionEntries.size == 1) {
                xAxisLabel.visibility = View.VISIBLE
                yAxisLabel.visibility = View.VISIBLE
                setupChartData(portionEntries)
            }
            refreshChart()
        } else {
            Toast.makeText(app, R.string.portion_add_fail, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onEditPortion(amountGrams: Float, exception: Exception? = null) {
        if (exception == null) {
            Toast.makeText(app, R.string.portion_edited, Toast.LENGTH_SHORT).show()

            deletePortionFromGraph()
            addPortionToGraph(amountGrams)
            setupChartSettings(portionEntries)
            refreshChart()
        } else {
            Toast.makeText(app, R.string.portion_edit_fail, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDeletePortion(exception: Exception? = null) {
        if (exception == null) {
            Toast.makeText(app, R.string.portion_deleted, Toast.LENGTH_SHORT).show()
            editPortionLayout.visibility = View.GONE
            addPortionLayout.visibility = View.VISIBLE

            deletePortionFromGraph()

            if (portionEntries.isEmpty()) {
                chart.clear()
                xAxisLabel.visibility = View.GONE
                yAxisLabel.visibility = View.GONE
            } else {
                setupChartSettings(portionEntries)
                refreshChart()
            }
        } else {
            Toast.makeText(app, R.string.portion_delete_fail, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addPortionToGraph(amount: Float) {
        //If the user amount as already been selected by other users
        userPortionEntry = portionEntries.firstOrNull {
            it.x == amount
        }
        //Add first amount data
        if (userPortionEntry == null) {
            userPortionEntry = BarEntry(amount, 1.toFloat())
            portionEntries.add(userPortionEntry!!)
        } else {
            userPortionEntry!!.y++
        }
        val currentPortions = viewModel.mealInfo!!.portions!!.allPortions
        viewModel.mealInfo!!.portions!!.allPortions = currentPortions.plus(amount)
        viewModel.mealInfo!!.portions!!.userPortion = amount
    }

    private fun deletePortionFromGraph() {
        val oldPortion = userPortionEntry!!
        oldPortion.y--
        if (oldPortion.y == 0.0F) {
            portionEntries.remove(oldPortion)
        }
        val currentPortions = viewModel.mealInfo!!.portions!!.allPortions
        viewModel.mealInfo!!.portions!!.allPortions = currentPortions.minus(oldPortion.x)
        viewModel.mealInfo!!.portions!!.userPortion = null
    }

    override fun setupChartData(entries: List<BarEntry>) {
        val graphDataSet = BarDataSet(entries, "Portions")
        val chartData = BarData(arrayListOf<IBarDataSet>(graphDataSet))
        chart.setupBarData(chartData)
    }

    override fun onEdit(onSuccess: () -> Unit) {
        sendToDestination(requireView(), Navigation.SEND_TO_ADD_CUSTOM_MEAL)
    }

    override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModel.putFavorite(
            mealItem = viewModel.mealInfo!!,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onReport(reportStr: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModel.report(
            mealItem = viewModel.mealInfo!!,
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
        viewModel.setVote(
            vote = voteState,
            userSession = requireUserSession(),
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putMealInfo(viewModel.mealInfo)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)    //Edit meal / calculator back
    }

    override fun fetchCtx(): Context = requireContext()
}