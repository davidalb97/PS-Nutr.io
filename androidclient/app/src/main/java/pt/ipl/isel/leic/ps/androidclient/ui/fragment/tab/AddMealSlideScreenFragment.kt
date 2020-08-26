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
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
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

    private lateinit var okButton: Button
    private lateinit var viewModel: IngredientListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Build ViewModel
        val intent = requireActivity().intent
        val factory = IngredientRecyclerVMProviderFactory(arguments, savedInstanceState, intent)
        viewModel = ViewModelProvider(this, factory)[IngredientListViewModel::class.java]
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
        fragments[setCheckArguments(IngredientsListFragment())] = "Meal Ingredients"
        fragments[setCheckArguments(MealItemListFragment())] = "Suggested Meals"
        fragments[setCheckArguments(FavoriteMealListFragment())] = "Favorite Meals"
        fragments[setCheckArguments(CustomMealListFragment())] = "Custom meals"
    }

    private fun <M: MealItem, F> setCheckArguments(fragment: F): F
            where F : BaseListFragment<M, *, *>, F: ICheckListenerOwner<M>, F: IItemClickListenerOwner<M> {

        //Configure checkbox module activation
        val bundle = Bundle()
        bundle.putItemActions(ItemAction.CHECK)
        bundle.putSource(Source.API)
        bundle.putMealItems(viewModel.checkedItems)
        fragment.arguments = bundle

        //Configure checkbox module listener
        fragment.onCheckListener = ICheckListener { item, isChecked ->
            val mealIngredient = toMealIngredient(item)
            //Add checked item to list
            if (isChecked) {
                //Change button state if it's the first item
                if (viewModel.items.isEmpty()) {
                    okButton.text = getString(R.string.ok)
                }
                //Ignore already checked items (list restore)
                else if(viewModel.items.any { it.submissionId == item.submissionId }) {
                    return@ICheckListener
                }
                viewModel.liveDataHandler.add(mealIngredient)
            }
            //Remove checked item from list
            else {
                //Change button state if it's last item
                if (viewModel.items.size == 1) {
                    okButton.text = getString(R.string.cancel)
                }
                //Find item to remove based on submission id
                //This will avoid failed removals when
                //the object is not the same after restoring checked items
                val itemToRemove = viewModel.items.first {
                    it.submissionId == mealIngredient.submissionId
                }
                viewModel.liveDataHandler.remove(itemToRemove)
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