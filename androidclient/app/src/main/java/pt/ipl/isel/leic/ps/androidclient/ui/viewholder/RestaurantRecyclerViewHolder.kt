package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant

class RestaurantRecyclerViewHolder(
    view: ViewGroup,
    val ctx: Context
) : ARecyclerViewHolder<Restaurant>(view) {

    val restaurantImage = view.findViewById<ImageView>(R.id.restaurantImage)
    val restaurantName = view.findViewById<TextView>(R.id.restaurantName)
    val restaurantCuisines = view.findViewById<TextView>(R.id.restaurantCuisines)
    val votesBar = view.findViewById<ProgressBar>(R.id.progressBar)
    val upvoteCounter = view.findViewById<TextView>(R.id.upVoteCounter)
    val downvoteCounter = view.findViewById<TextView>(R.id.downVoteCounter)
    val upvoteButton = view.findViewById<ImageButton>(R.id.upvote)
    val dowvoteButton = view.findViewById<ImageButton>(R.id.downvote)
    val favoriteButton = view.findViewById<ImageButton>(R.id.favorite)
    val optionsButton = view.findViewById<ImageButton>(R.id.options)


    override fun bindTo(item: Restaurant) {
        super.bindTo(item)
        //restaurantImage.setImageResource(item.)
        restaurantName.text = item.name

        itemView.setOnClickListener {
            val bundle = Bundle()
            view.findNavController().navigate(R.id.nav_restaurant_detail)
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}