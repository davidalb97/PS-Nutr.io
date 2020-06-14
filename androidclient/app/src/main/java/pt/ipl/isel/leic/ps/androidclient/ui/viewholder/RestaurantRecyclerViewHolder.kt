package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_RESTAURANT_INFO_ID
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MEAL_LIST_RESTAURANT_ID
import pt.ipl.isel.leic.ps.androidclient.ui.provider.RESTAURANT_LIST_VIEW_STATE

class RestaurantRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ARecyclerViewHolder<RestaurantItem>(view, ctx),
    View.OnClickListener {

    val restaurantImage = view.findViewById<ImageView>(R.id.restaurantImage)
    val restaurantName = view.findViewById<TextView>(R.id.restaurantName)
    val votesBar = view.findViewById<ProgressBar>(R.id.progressBar)
    val upvoteCounter = view.findViewById<TextView>(R.id.upVoteCounter)
    val downvoteCounter = view.findViewById<TextView>(R.id.downVoteCounter)
    val upvoteButton = view.findViewById<ImageButton>(R.id.upvote)
    val dowvoteButton = view.findViewById<ImageButton>(R.id.downvote)
    val favoriteButton = view.findViewById<ImageButton>(R.id.favorite)
    val optionsButton = view.findViewById<ImageButton>(R.id.options)


    override fun bindTo(item: RestaurantItem) {
        super.bindTo(item)
        setupViewHolderElements()
        setupListeners()
    }

    private fun setupViewHolderElements() {
        if (item.imageUri.toString().isBlank()) {
            itemView.visibility = View.GONE
        } else {
            //TODO add image
        }

        restaurantName.text = item.name
        val votes = item.votes
        if (votes != null) {
            val upvotes = votes.positive
            val downvotes = votes.negative
            votesBar.progress =
                upvotes / (upvotes + downvotes) * 100
            upvoteCounter.text = upvotes.toString()
            downvoteCounter.text = downvotes.toString()
        }
    }

    private fun setupListeners() {
        this.view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        itemView.setOnClickListener {
            val bundle = bundleOf(Pair(BUNDLE_RESTAURANT_INFO_ID, item.id))
            view.findNavController().navigate(R.id.nav_restaurant_detail, bundle)
        }
    }
}