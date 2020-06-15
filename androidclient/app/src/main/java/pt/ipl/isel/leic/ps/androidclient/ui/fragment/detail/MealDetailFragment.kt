package pt.ipl.isel.leic.ps.androidclient.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.provider.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealInfoViewModel

class MealDetailFragment : Fragment(){

    private val log = Logger(MealDetailFragment::class.java)
    lateinit var viewModel: MealInfoViewModel

    private var receivedMeal: MealInfo? = null

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = CalculatorVMProviderFactory(savedInstanceState, rootActivity.intent, arguments)
        viewModel = ViewModelProvider(rootActivity, factory)[MealInfoViewModel::class.java]

        //Read passed info from bundle
        viewModel.mealInfo = arguments?.getParcelable(BUNDLE_MEAL_INFO)
        viewModel.source = arguments?.getInt(BUNDLE_MEAL_SOURCE, -1)?.let {
            if(it == -1) null else Source.values()[it]
        }
        viewModel.submissionId = arguments?.getInt(BUNDLE_MEAL_SUBMISSION_ID, -1)?.let {
            if(it == -1) null else it
        }
        viewModel.dbId = arguments?.getLong(BUNDLE_MEAL_DB_ID, -1)?.let {
            val check: Long = -1
            if(it == check) null else it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.meal_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setOnErrorFunc(log::e)
        viewModel.observe(this) {
            val mealInfo = it.first()
            receivedMeal = mealInfo
            setupView(mealInfo)
        }
        viewModel.update()
    }

    private fun setupView(receivedMeal: MealInfo) {
        val mealMealImage = view?.findViewById<ImageView>(R.id.meal_detail_image)
        val mealMealTitle = view?.findViewById<TextView>(R.id.meal_detail_title)
        val mealVotesRl = view?.findViewById<RelativeLayout>(R.id.meal_info_votes)
        val mealUpvoteButton = view?.findViewById<Button>(R.id.meal_info_up_vote)
        val mealDownvoteButton = view?.findViewById<Button>(R.id.meal_info_down_vote)
        val mealShowIngredientsBtn = view?.findViewById<Button>(R.id.meal_info_show_ingredients_btn)
        val mealIngredientsRl = view?.findViewById<RelativeLayout>(R.id.meal_info_ingredients_rl)
        val mealAddIngredientImgBtn = view?.findViewById<ImageButton>(R.id.meal_info_add_ingredients_button)
        val mealSuggestedRl = view?.findViewById<RelativeLayout>(R.id.meal_info_suggested_rl)

        // Image loading
        if (receivedMeal.imageUri == null)
            mealMealImage?.visibility = View.GONE
        else
            Glide.with(requireView())
                .load(receivedMeal.imageUri)
                .into(mealMealImage!!)

        mealMealTitle?.text = receivedMeal.name

        if(receivedMeal.source != Source.CUSTOM) {
            //Show suggested txt
            if(receivedMeal.isSuggested) {
                mealSuggestedRl?.visibility = View.VISIBLE
            }

            //Show votes
            mealVotesRl?.visibility = View.VISIBLE

            // Upvote
            mealUpvoteButton?.setOnClickListener { view ->
                viewModel.upVote()
            }

            // Downvote
            mealDownvoteButton?.setOnClickListener { view ->
                viewModel.downVote()
            }
        } else {
            mealAddIngredientImgBtn?.visibility = View.VISIBLE
            mealAddIngredientImgBtn?.setOnClickListener {
                //TODO Navigate to fragment and select meals & ingredients to add to ingredient recycler
            }
        }

        mealShowIngredientsBtn?.setOnClickListener {
            mealIngredientsRl?.visibility?.also { visibility ->
                mealIngredientsRl.visibility = if(visibility == View.GONE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}