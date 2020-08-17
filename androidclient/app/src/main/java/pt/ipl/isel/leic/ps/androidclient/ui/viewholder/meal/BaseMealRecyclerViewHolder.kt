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
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.AMenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IPopupMenuButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
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
    IReportMenuItem {

    private val mealName: TextView = view.findViewById(R.id.mealName)
    override val menus: MutableList<AMenuItemFactory> = mutableListOf()
    override val menuButton: ImageButton = view.findViewById(R.id.options)
    override val image: ImageView = view.findViewById(R.id.mealImage)
    override val pressActionView: RelativeLayout = view.findViewById(R.id.actions_layout)
    override val voteCountersLayout: ViewGroup = view.findViewById(R.id.vote_counters_layout)
    override val votesBar: ProgressBar = view.findViewById(R.id.votesBar)
    override val upVoteCounter: TextView = view.findViewById(R.id.upVoteCounter)
    override val downVoteCounter: TextView = view.findViewById(R.id.downVoteCounter)
    override val favoriteButton: ImageButton = view.findViewById(R.id.favorite)
    override val calculatorButton: ImageButton = view.findViewById(R.id.add_to_calc_action)
    override val deleteButton: ImageButton = view.findViewById(R.id.delete_item_action)

    override fun bindTo(item: T) {
        super.bindTo(item)
        mealName.text = item.name

        super.setupCalculateAction(view)
        super.setupPressAction(view)
        super.setupOnDeleteAction(bindingAdapter, layoutPosition)
        super.setupFavoriteButton()
        super.setupImage(view, item.imageUri)
        super.setupVoteBarCounters(item.votes)
        super.setupReportMenuItem()
        super.setupPopupMenuButton()
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