package pt.ipl.isel.leic.ps.androidclient.ui.fragment.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.IngredientsRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealInfoViewModel

class MealDetailFragment : IngredientsRecyclerFragment() {

    private val log = Logger(MealDetailFragment::class.java)
    lateinit var viewModelMealInfo: MealInfoViewModel

    private var receivedMeal: MealInfo? = null

    override fun buildViewModel(savedInstanceState: Bundle?) {
        super.buildViewModel(savedInstanceState)

        val rootActivity = this.requireActivity()
        val factoryMealInfo =
            MealInfoVMProviderFactory(savedInstanceState, rootActivity.intent, arguments)
        viewModelMealInfo =
            ViewModelProvider(rootActivity, factoryMealInfo)[MealInfoViewModel::class.java]

        //Read passed info from bundle
        viewModelMealInfo.mealInfo = arguments?.getParcelable(BUNDLE_MEAL_INFO)
        viewModelMealInfo.source = arguments?.getInt(BUNDLE_MEAL_SOURCE, -1)?.let {
            if (it == -1) null else Source.values()[it]
        }
        viewModelMealInfo.submissionId = arguments?.getInt(BUNDLE_MEAL_SUBMISSION_ID, -1)?.let {
            if (it == -1) null else it
        }
        viewModelMealInfo.restaurantId = arguments?.getString(BUNDLE_MEAL_RESTAURANT_SUBMISSION_ID)
        viewModelMealInfo.dbId = arguments?.getLong(BUNDLE_MEAL_DB_ID, -1)?.let {
            val check: Long = -1
            if (it == check) null else it
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
        viewModelMealInfo.observe(this) {
            val mealInfo = it.first()
            receivedMeal = mealInfo
            setupView(mealInfo)
            viewModel.ingredients = mealInfo.ingredientComponents.plus(mealInfo.mealComponents)
            super.onViewCreated(view, savedInstanceState)
        }
        viewModelMealInfo.update()
    }

    private fun setupView(receivedMeal: MealInfo) {
        val mealMealImage = view?.findViewById<ImageView>(R.id.meal_detail_image)
        val mealMealTitle = view?.findViewById<TextView>(R.id.meal_detail_title)
        val mealVotesRl = view?.findViewById<RelativeLayout>(R.id.meal_info_votes)
        val mealUpvoteButton = view?.findViewById<Button>(R.id.meal_info_up_vote)
        val mealDownvoteButton = view?.findViewById<Button>(R.id.meal_info_down_vote)
        val mealShowIngredientsBtn = view?.findViewById<Button>(R.id.meal_info_show_ingredients_btn)
        val mealIngredientsRl = view?.findViewById<RelativeLayout>(R.id.meal_info_ingredients_rl)
        val mealAddIngredientImgBtn =
            view?.findViewById<ImageButton>(R.id.meal_info_add_ingredients_button)
        val mealSuggestedRl = view?.findViewById<RelativeLayout>(R.id.meal_info_suggested_rl)

        // Image loading
        if (receivedMeal.imageUri == null)
            mealMealImage?.visibility = View.GONE
        else
            Glide.with(requireView())
                .load(receivedMeal.imageUri)
                .into(mealMealImage!!)

        mealMealTitle?.text = receivedMeal.name

        if (receivedMeal.source != Source.CUSTOM) {
            //Show suggested txt
            if (receivedMeal.isSuggested) {
                mealSuggestedRl?.visibility = View.VISIBLE
            } else {
                //Show votes
                mealVotesRl?.visibility = View.VISIBLE

                val userSession = UserSession(
                    sharedPreferences.getString("jwt", "")!!
                )

                // Upvote
                mealUpvoteButton?.setOnClickListener { view ->
                    viewModelMealInfo.setVote(
                        vote = true,
                        success = {
                            Toast.makeText(
                                app,
                                "Upvoted!",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        error = log::e,
                        userSession = userSession
                    )
                }

                // Downvote
                mealDownvoteButton?.setOnClickListener { view ->
                    viewModelMealInfo.setVote(
                        vote = true,
                        success = {
                            Toast.makeText(
                                app,
                                "Downvoted!",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        error = log::e,
                        userSession = userSession
                    )
                }
            }
        } else {
            mealAddIngredientImgBtn?.visibility = View.VISIBLE
            mealAddIngredientImgBtn?.setOnClickListener {

                //TODO Navigate to fragment and select meals & ingredients to add to ingredient recycler
            }
        }

        mealShowIngredientsBtn?.setOnClickListener {
            mealIngredientsRl?.visibility?.also { visibility ->
                mealIngredientsRl.visibility = if (visibility == View.GONE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

//    private fun restaurantMealOperation(onAssociated: () -> Unit) {
//        if(!receivedMeal!!.isSuggested) {
//            onAssociated()
//        } else {
//            viewModelMealInfo.
//        }
//    }

    override fun getRecyclerId() = R.id.meal_info_ingredient_item_list

    override fun getProgressBarId() = R.id.meal_info_progress_bar
}