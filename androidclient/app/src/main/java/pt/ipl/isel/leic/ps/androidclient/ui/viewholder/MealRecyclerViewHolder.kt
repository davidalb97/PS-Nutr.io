package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_MEAL_DB_ID
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_MEAL_SUBMISSION_ID
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_MEAL_SOURCE

class MealRecyclerViewHolder(view: ViewGroup, ctx: Context) :
    ARecyclerViewHolder<MealItem>(view, ctx),
    IFavorable<MealItem>,
    ICalculatable<MealItem> {

    val mealImage = view.findViewById<ImageView>(R.id.mealImage)
    val mealName = view.findViewById<TextView>(R.id.mealName)
    val votesBar = view.findViewById<ProgressBar>(R.id.progressBar)
    val upvoteCounter = view.findViewById<TextView>(R.id.upVoteCounter)
    val downvoteCounter = view.findViewById<TextView>(R.id.downVoteCounter)
    override var favoriteButton = view.findViewById<ImageButton>(R.id.favorite)
    override var calculatorButton: ImageButton = view.findViewById(R.id.add_meal_to_calc)
    val optionsButton = view.findViewById<ImageButton>(R.id.options)

    override lateinit var onFavorite: (MealItem) -> AsyncWorker<Unit, Unit>

    override fun bindTo(item: MealItem) {
        super.bindTo(item)
        setupViewHolderElements()
        setupListeners()
    }

    private fun setupViewHolderElements() {

        if (item.imageUri == null) {
            mealImage.visibility = View.GONE
        } else {
            Glide.with(itemView)
                .load(item.imageUri)
                .into(mealImage)
        }

        mealName.text = item.name
        val votes = item.votes
        if (votes != null) {
            val upvotes = votes.positive
            val downvotes = votes.negative
            val totalVotes = upvotes + downvotes
            votesBar.progress = 0
            if (totalVotes > 0)
                votesBar.progress =
                    upvotes / totalVotes * 100
            upvoteCounter.text = upvotes.toString()
            downvoteCounter.text = downvotes.toString()
        }
    }

    private fun setupListeners() {
        favoriteButton.setOnClickListener {
            val favoriteItem =
                MealItem(
                    dbId = item.dbId,
                    dbRestaurantId = item.dbRestaurantId,
                    submissionId = item.submissionId,
                    name = item.name,
                    carbs = item.carbs,
                    amount = item.amount,
                    unit = item.unit,
                    votes = item.votes,
                    isFavorite = item.isFavorite,
                    imageUri = item.imageUri,
                    isSuggested = item.isSuggested,
                    source = Source.FAVORITE
                )
            onFavorite(favoriteItem)
                .setOnPostExecute {
                    Toast.makeText(
                        ctx,
                        "Added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .execute()
        }

        this.view.setOnClickListener(this)
        this.view.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        setButtonsVisibility(false)
        if (isCalculatorMode) {
            sendToCalculator()
        } else {
            sendToMealDetail()
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
        bundle.putInt(BUNDLE_MEAL_SOURCE, this.item.source.ordinal)
        bundle.putInt(BUNDLE_MEAL_SUBMISSION_ID, this.item.submissionId)
        bundle.putLong(BUNDLE_MEAL_DB_ID, this.item.dbId)
        view.findNavController().navigate(R.id.nav_calculator, bundle)
    }

    private fun sendToMealDetail() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_MEAL_SOURCE, this.item.source.ordinal)
        bundle.putInt(BUNDLE_MEAL_SUBMISSION_ID, this.item.submissionId)
        bundle.putLong(BUNDLE_MEAL_DB_ID, this.item.dbId)
        view.findNavController().navigate(R.id.nav_meal_detail, bundle)
    }
}