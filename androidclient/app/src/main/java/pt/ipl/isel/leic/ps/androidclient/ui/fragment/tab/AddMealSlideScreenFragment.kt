package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
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
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddIngredientsVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.AddIngredientsListViewModel

class AddMealSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false), ISend {

    private lateinit var okButton: Button
    private lateinit var viewModel: AddIngredientsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Build ViewModel
        val intent = requireActivity().intent
        val factory = AddIngredientsVMProviderFactory(arguments, savedInstanceState, intent)
        viewModel = ViewModelProvider(this, factory)[AddIngredientsListViewModel::class.java]
        //Restore picked items to items list
        viewModel.update()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        okButton = view.findViewById(R.id.tab_fragment_ok_btn)
        okButton.text = getString(if (viewModel.items.isEmpty()) R.string.cancel else R.string.ok)
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

    private fun <M : MealItem, F> setCheckArguments(fragment: F): F
            where F : BaseListFragment<M, *, *>, F : ICheckListenerOwner<M>, F : IItemClickListenerOwner<M> {

        //Configure checkbox module activation
        val bundle = Bundle()
        bundle.putItemActions(ItemAction.CHECK)
        bundle.putSource(Source.API)

        fragment.arguments = bundle
        fragment.restoredItemPredicator = ::restoredItemPredicator
        fragment.onCheckListener = getCheckListener()
        fragment.onClickListener = getItemClickListener()

        return fragment
    }

    override fun onSendToDestination(bundle: Bundle) {
        if (viewModel.items.isNotEmpty()) {
            bundle.putMealIngredients(viewModel.items)
        }
    }

    private fun <T : MealItem> restoredItemPredicator(item: T): Boolean {
        val originalItem = viewModel.items.firstOrNull {
            it.submissionId == item.submissionId
        }
        return if (originalItem != null) {
            item.amount = originalItem.amount
            item.carbs = originalItem.carbs
            true
        } else false
    }

    private fun <T: MealItem> getCheckListener(): ICheckListener<T> {
        return ICheckListener { item, isChecked, onChangeCallback ->
            val mealIngredient = toMealIngredient(item)
            val existingItem = viewModel.items.firstOrNull { it.submissionId == item.submissionId }
            //Add checked item to list
            if (isChecked) {
                //Change button state if it's the first item
                if (viewModel.items.isEmpty()) {
                    okButton.text = getString(R.string.ok)
                }
                //Ignore already checked items (list restore)
                else if (existingItem != null) {
                    item.carbs = existingItem.carbs
                    item.amount = existingItem.amount
                    return@ICheckListener
                }
                MealAmountSelector(
                    ctx = requireContext(),
                    layoutInflater = layoutInflater,
                    baseCarbs = item.carbs.toFloat(),
                    baseAmountGrams = item.amount.toFloat(),
                    mealUnit = WeightUnits.fromValue(item.unit)
                ) { preciseGrams, preciseCarbs ->
                    val roundedGrams = preciseGrams.toInt()
                    val roundedCarbs = preciseCarbs.toInt()
                    item.amount = roundedGrams
                    item.carbs = roundedCarbs
                    mealIngredient.amount = roundedGrams
                    mealIngredient.carbs = roundedCarbs
                    viewModel.liveDataHandler.add(mealIngredient)
                    onChangeCallback()
                }
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
                viewModel.liveDataHandler.remove(existingItem ?: mealIngredient)
            }
        }
    }

    private fun <T: MealItem> getItemClickListener(): IItemClickListener<T> {
        return IItemClickListener { item, onChangeCallback ->
            val mealIngredient =
                viewModel.items.firstOrNull { it.submissionId == item.submissionId }
            if (mealIngredient != null) {
                MealAmountSelector(
                    ctx = requireContext(),
                    layoutInflater = layoutInflater,
                    baseCarbs = mealIngredient.carbs.toFloat(),
                    baseAmountGrams = mealIngredient.amount.toFloat(),
                    mealUnit = WeightUnits.fromValue(item.unit)
                ) { preciseGrams, preciseCarbs ->
                    val roundedGrams = preciseGrams.toInt()
                    val roundedCarbs = preciseCarbs.toInt()
                    item.amount = roundedGrams
                    item.carbs = roundedCarbs
                    mealIngredient.amount = roundedGrams
                    mealIngredient.carbs = roundedCarbs
                    onChangeCallback()
                }
            }
        }
    }

    private fun toMealIngredient(mealItem: MealItem): MealIngredient {
        return if (mealItem is MealIngredient) {
            mealItem.dbMealId = arguments?.getDbId()
            mealItem
        } else MealIngredient(
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