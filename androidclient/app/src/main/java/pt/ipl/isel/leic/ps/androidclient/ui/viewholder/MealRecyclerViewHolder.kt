package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

// TODO: Create Cuisines
class MealRecyclerViewHolder(view: ViewGroup, ctx: Context) : ARecyclerViewHolder<Meal>(view) {
    val mealImage = view.findViewById<ImageView>(R.id.restaurantImage)
    val mealName = view.findViewById<TextView>(R.id.restaurantName)
    //val restaurantCuisines = view.findViewById<TextView>(R.id.restaurantCuisines)
    val votesBar = view.findViewById<ProgressBar>(R.id.progressBar)
    val upvoteCounter = view.findViewById<TextView>(R.id.upVoteCounter)
    val downvoteCounter = view.findViewById<TextView>(R.id.downVoteCounter)
    val upvoteButton = view.findViewById<ImageButton>(R.id.upvote)
    val dowvoteButton = view.findViewById<ImageButton>(R.id.downvote)
    val favoriteButton = view.findViewById<ImageButton>(R.id.favorite)
    val optionsButton = view.findViewById<ImageButton>(R.id.options)

    override fun bindTo(meal: Meal) {
        super.bindTo(meal)

        mealName.text = meal.name
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(v: View?): Boolean {
        TODO("Not yet implemented")
    }
}