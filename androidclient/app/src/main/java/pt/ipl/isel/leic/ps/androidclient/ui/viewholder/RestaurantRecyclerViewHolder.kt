package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_RESTAURANT_INFO_ID

class RestaurantRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ARecyclerViewHolder<RestaurantItem>(view, ctx),
    View.OnClickListener {

    val restaurantImage = view.findViewById<ImageView>(R.id.mealImage)
    val restaurantName = view.findViewById<TextView>(R.id.mealName)
    val votesBar = view.findViewById<ProgressBar>(R.id.votesBar)
    val upvoteCounter = view.findViewById<TextView>(R.id.upVoteCounter)
    val downvoteCounter = view.findViewById<TextView>(R.id.downVoteCounter)
    val upvoteButton = view.findViewById<ImageButton>(R.id.upvote)
    val dowvoteButton = view.findViewById<ImageButton>(R.id.downvote)
    val favoriteButton = view.findViewById<ImageButton>(R.id.favorite)
    val optionsButton = view.findViewById<ImageButton>(R.id.options)
    val votesRl = view.findViewById<RelativeLayout>(R.id.restaurant_card_votes_rl)


    override fun bindTo(item: RestaurantItem) {
        super.bindTo(item)
        setupViewHolderElements()
        setupListeners()
    }

    private fun setupViewHolderElements() {
        if (item.imageUri == null) {
            restaurantImage.visibility = View.GONE
        } else {
            Glide.with(itemView)
                .load(item.imageUri)
                .into(restaurantImage)
        }

        restaurantName.text = item.name
        val votes = item.votes
        if (votes != null) {
            votesRl.visibility = View.VISIBLE
            val upvotes = votes.positive
            val downvotes = votes.negative
            val totalVotes = upvotes + downvotes
            votesBar.progress = 0
            if (totalVotes > 0)
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
        val bundle = Bundle()
        bundle.putString(BUNDLE_RESTAURANT_INFO_ID, item.id)
        view.findNavController().navigate(R.id.nav_restaurant_detail, bundle)
    }
}