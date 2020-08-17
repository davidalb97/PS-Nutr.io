package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IImage
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IVoteProgress
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IFavoriteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.AMenuItemFactory
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IPopupMenuButton
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu.IReportMenuItem
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
    override val menus: MutableList<AMenuItemFactory> = mutableListOf()
    override val menuButton: ImageButton = view.findViewById(R.id.options)
    override val image: ImageView = view.findViewById(R.id.restaurantImage)
    override val votesBar: ProgressBar = view.findViewById(R.id.votesBar)
    override val voteCountersLayout: RelativeLayout =
        view.findViewById(R.id.restaurant_card_votes_bar)
    override val upVoteCounter: TextView = view.findViewById(R.id.upVoteCounter)
    override val downVoteCounter: TextView = view.findViewById(R.id.downVoteCounter)
    override val favoriteButton: ImageButton = view.findViewById(R.id.favorite)

    override fun bindTo(item: RestaurantItem) {
        super.bindTo(item)

        restaurantName.text = item.name
        super.setupImage(view, item.imageUri)
        super.setupVoteBarCounters(item.votes)
        super.setupFavoriteButton()
        super.setupReportMenuItem()
        super.setupPopupMenuButton()
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putItemActions(actions)
        bundle.putNavigation(navDestination)
        bundle.putRestaurantItem(item)
    }

    override fun isFavorite(): Boolean = item.isFavorite
}