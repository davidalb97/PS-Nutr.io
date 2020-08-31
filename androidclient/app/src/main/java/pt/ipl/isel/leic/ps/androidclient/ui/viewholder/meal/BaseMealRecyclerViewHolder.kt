package pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IImage
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IVoteProgress
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.ICalculatorActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IDeleteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IFavoriteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.MenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IPopupMenuButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckBox
import pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder.IMealItemDetail
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.BaseRecyclerViewHolder

abstract class BaseMealRecyclerViewHolder<T : MealItem>(
    override val actions: List<ItemAction>,
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseRecyclerViewHolder<T>(
    navDestination = navDestination,
    view = view,
    ctx = ctx
), ICalculatorActionButton,
    IVoteProgress,
    IImage,
    IDeleteActionButton<T>,
    IFavoriteActionButton,
    IPopupMenuButton,
    IReportMenuItem,
    IMealItemDetail<T>,
    ICheckBox<T> {

    private val mealName: TextView = view.findViewById(R.id.mealName)
    override val checkBoxId: Int = R.id.meal_card_checkbox
    override lateinit var checkBox: CheckBox
    override val customMealQuantityId: Int = R.id.custom_meal_quantity
    override lateinit var customMealQuantity: TextView
    override val customMealCarbsId: Int = R.id.custom_meal_carbs_amount
    override lateinit var customMealCarbs: TextView
    override val menus: MutableMap<String, MenuItemFactory> = mutableMapOf()
    override val menuButtonId = R.id.options
    override lateinit var menuButton: ImageButton
    override val imageId: Int = R.id.mealImage
    override lateinit var image: ImageView
    override val pressActionViewId: Int = R.id.actions_layout
    override lateinit var pressActionView: RelativeLayout
    override val voteCountersLayoutId: Int = R.id.vote_counters_layout
    override lateinit var voteCountersLayout: ViewGroup
    override val votesBarId: Int = R.id.votesBar
    override lateinit var votesBar: ProgressBar
    override val upVoteCounterId: Int = R.id.upVoteCounter
    override lateinit var upVoteCounter: TextView
    override val downVoteCounterId: Int = R.id.downVoteCounter
    override lateinit var downVoteCounter: TextView
    override val favoriteButtonId: Int = R.id.favorite
    override lateinit var favoriteButton: ImageButton
    override val calculatorButtonId: Int = R.id.add_to_calc_action
    override lateinit var calculatorButton: ImageButton
    override val deleteButtonId: Int = R.id.delete_item_action
    override lateinit var deleteButton: ImageButton

    override fun bindTo(item: T) {
        super.bindTo(item)
        mealName.text = item.name

        super.setupCheckBox(view, item)
        super.setupCustomMeal(view, ctx, item)
        super.setupCalculateAction(view)
        super.setupPressAction(view)
        super.setupOnDeleteAction(view, bindingAdapter, layoutPosition)
        super.setupFavoriteButton(view)
        super.setupImage(view, item.imageUri)
        super.setupVoteBarCounters(view, item.votes, item.isVotable)
        super.setupReportMenuItem()
        super.setupPopupMenuButton(view)
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putItemActions(actions)
        bundle.putNavigation(navDestination)
        bundle.putSource(this.item.source)
        bundle.putMealSubmissionId(this.item.submissionId)
        bundle.putRestaurantSubmissionId(this.item.restaurantSubmissionId)
        bundle.putDbId(this.item.dbId)
    }

    override fun isFavorite(): Boolean = item.isFavorite
}