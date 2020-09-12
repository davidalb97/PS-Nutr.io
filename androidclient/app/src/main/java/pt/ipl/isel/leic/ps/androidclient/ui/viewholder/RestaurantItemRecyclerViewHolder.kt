package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IImage
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IVoteProgress
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IFavoriteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IPopupMenuButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.MenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*

abstract class RestaurantItemRecyclerViewHolder(
    override val actions: List<ItemAction>,
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseRecyclerViewHolder<RestaurantItem>(
    navDestination = navDestination,
    view = view,
    ctx = ctx
),
    IImage,
    IVoteProgress,
    IFavoriteActionButton,
    IPopupMenuButton,
    IReportMenuItem {

    private val restaurantName: TextView = view.findViewById(R.id.restaurantName)
    override val menus: MutableMap<String, MenuItemFactory> = mutableMapOf()
    override val menuButtonId: Int = R.id.options
    override lateinit var menuButton: ImageButton
    override val imageId: Int = R.id.restaurantImage
    override lateinit var image: ImageView
    override val votesBarId: Int = R.id.votesBar
    override lateinit var votesBar: ProgressBar
    override val voteCountersLayoutId: Int = R.id.restaurant_card_votes_bar
    override lateinit var voteCountersLayout: ViewGroup
    override val upVoteCounterId: Int = R.id.upVoteCounter
    override lateinit var upVoteCounter: TextView
    override val downVoteCounterId: Int = R.id.downVoteCounter
    override lateinit var downVoteCounter: TextView
    override val favoriteButtonId: Int = R.id.favorite
    override lateinit var favoriteButton: ImageButton

    override fun bindTo(item: RestaurantItem) {
        super.bindTo(item)

        restaurantName.text = item.name
        super.setupImage(view, item.image)
        super.setupVoteBarCounters(view, item.votes)
        super.setupFavoriteButton(view, item.favorites)
        super.setupReportMenuItem(item.isReportable)
        super.setupPopupMenuButton(view)
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putItemActions(actions)
        bundle.putNavigation(navDestination)
        bundle.putRestaurantItem(item)
    }
}