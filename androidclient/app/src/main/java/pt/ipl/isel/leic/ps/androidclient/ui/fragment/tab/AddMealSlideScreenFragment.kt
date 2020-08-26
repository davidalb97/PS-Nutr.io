package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.*
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.IngredientRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.IngredientListViewModel

class AddMealSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false), ISend {

    private lateinit var ingredientsListFragment: IngredientsListFragment
    private lateinit var mealItemListFragment: MealItemListFragment
    private lateinit var favoriteMealListFragment: FavoriteMealListFragment
    private lateinit var customMealListFragment: CustomMealListFragment
    private lateinit var okButton: Button
    private lateinit var viewModel: IngredientListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Build ViewModel
        val intent = requireActivity().intent
        val factory = IngredientRecyclerVMProviderFactory(arguments, savedInstanceState, intent)
        viewModel = ViewModelProvider(this, factory)[IngredientListViewModel::class.java]

        ingredientsListFragment = setCheckArguments(IngredientsListFragment())
        mealItemListFragment = setCheckArguments(MealItemListFragment())
        favoriteMealListFragment = setCheckArguments(FavoriteMealListFragment())
        customMealListFragment = setCheckArguments(CustomMealListFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        okButton = view.findViewById(R.id.tab_fragment_ok_btn)
        okButton.visibility = View.VISIBLE
        okButton.setOnClickListener {
            sendToDestination(view, Navigation.SEND_TO_ADD_CUSTOM_MEAL)
        }
    }

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[ingredientsListFragment] = "Meal Ingredients"
        fragments[mealItemListFragment] = "Suggested Meals"
        fragments[favoriteMealListFragment] = "Favorite Meals"
        fragments[customMealListFragment] = "Custom meals"
    }

    private fun <M: MealItem, F> setCheckArguments(fragment: F): F
            where F : BaseListFragment<M, *, *>, F: ICheckListenerOwner<M>, F: IItemClickListenerOwner<M> {

        //Configure checkbox module activation
        val bundle = Bundle()
        bundle.putItemActions(ItemAction.CHECK)
        bundle.putMealItems(viewModel.checkedItems)
        fragment.arguments = bundle

        //Configure checkbox module listener
        fragment.onCheckListener = ICheckListener { item, isChecked ->
            val mealIngredient = toMealIngredient(item)
            if (isChecked) {
                if (viewModel.items.isEmpty()) {
                    okButton.text = getString(R.string.ok)
                }
                viewModel.liveDataHandler.add(mealIngredient)
            } else {
                if (viewModel.items.size == 1) {
                    okButton.text = getString(R.string.cancel)
                }
                viewModel.liveDataHandler.remove(mealIngredient)
            }
        }
        //Configure checkbox module listener
        fragment.onClickListener = IItemClickListener { item, idx ->
            PromptInput(
                ctx = requireContext(),
                titleId = R.string.select_ingredient_amount,
                inputType = InputType.TYPE_CLASS_NUMBER,
                confirmConsumer = {
                    item.amount = it.toInt()
                    fragment.recyclerAdapter.notifyItemChanged(idx)
                }
            ).show()
        }

        return fragment
    }

    override fun onSendToDestination(bundle: Bundle) {
        if (viewModel.items.isNotEmpty()) {
            bundle.putMealIngredients(viewModel.items)
        }
    }

    private fun toMealIngredient(mealItem: MealItem): MealIngredient {
        return if (mealItem is MealIngredient) {
            mealItem.dbMealId = arguments?.getDbId()
            mealItem
        }
        else MealIngredient(
            dbMealId = arguments?.getDbId(),
            //Ingredient fragment is the only fragment that returns meal ingredients
            isMeal = true,
            dbId = mealItem.dbId,
            submissionId = mealItem.submissionId,
            name = mealItem.name,
            carbs = mealItem.carbs,
            amount = mealItem.amount,
            unit = mealItem.unit,
            imageUri = mealItem.imageUri,
            source = mealItem.source
        )
    }
}