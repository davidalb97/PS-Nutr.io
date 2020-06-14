package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.BUNDLED_MEAL_TAG

class MealRecyclerViewHolder(view: ViewGroup, ctx: Context) :
    ARecyclerViewHolder<MealItem>(view, ctx),
    IFavorable<MealItem>,
    ICalculatable<MealItem>{

    val mealImage = view.findViewById<ImageView>(R.id.restaurantImage)
    val mealName = view.findViewById<TextView>(R.id.restaurantName)
    val votesBar = view.findViewById<ProgressBar>(R.id.progressBar)
    val upvoteCounter = view.findViewById<TextView>(R.id.upVoteCounter)
    val downvoteCounter = view.findViewById<TextView>(R.id.downVoteCounter)
    override var favoriteButton = view.findViewById<ImageButton>(R.id.favorite)
    override var calculatorButton: ImageButton = view.findViewById(R.id.add_custom_meal_to_calc)
    val optionsButton = view.findViewById<ImageButton>(R.id.options)

    override lateinit var onFavorite: (MealItem) -> AsyncWorker<Unit, Unit>

    override fun bindTo(item: MealItem) {
        super.bindTo(item)
        mealName.text = item.name
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

    override fun onClick(v: View?) {
        setButtonsVisibility(false)
        if (isCalculatorMode) {
            sendToCalculator()
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (!isCalculatorMode) {
            setButtonsVisibility(true)
            calculatorButton.setOnClickListener {
                sendToCalculator()
            }
            return true
        }
        return true
    }

    override fun setButtonsVisibility(isVisible: Boolean) {
        val visibility =
            if (isVisible) View.VISIBLE else View.INVISIBLE
        calculatorButton.visibility = visibility
    }

    private fun sendToCalculator() {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLED_MEAL_TAG, this.item)
        view.findNavController().navigate(R.id.nav_calculator, bundle)
    }
}