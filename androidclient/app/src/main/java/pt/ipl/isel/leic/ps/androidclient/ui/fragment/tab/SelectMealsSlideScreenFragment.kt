package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.IParentViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.*
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.navParentViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseAddMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.getNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.prompt.MealAmountSelector
import pt.ipl.isel.leic.ps.androidclient.ui.util.putItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.util.putSource
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.MealItemPickViewModel

class SelectMealsSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false),
    IParentViewModel {

    private lateinit var okButton: Button
    override var savedInstanceState: Bundle? = null
    override val vMProviderFactorySupplier = ::BaseAddMealRecyclerVMProviderFactory
    private val viewModel: MealItemPickViewModel by navParentViewModel()

    init {
        retainInstance = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Restore picked items to items list
        viewModel.setupList()

        okButton = view.findViewById(R.id.tab_fragment_ok_btn)
        okButton.text =
            getString(if (viewModel.pickedItems.isEmpty()) R.string.cancel else R.string.ok)
        okButton.visibility = View.VISIBLE
        okButton.setOnClickListener {
            //Navigate to parent pop
            navigate(requireArguments().getNavigation())
        }
    }

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[setCheckArguments(IngredientsListFragment(), Source.API)] = "Meal Ingredients"
        fragments[setCheckArguments(MealItemListFragment(), Source.API)] = "Suggested Meals"
        fragments[setCheckArguments(FavoriteMealListFragment(), Source.FAVORITE_MEAL)] =
            "Favorite Meals"
        fragments[setCheckArguments(FavoriteMealListFragment(), Source.FAVORITE_RESTAURANT_MEAL)] =
            "Favorite Restaurant Meals"
        fragments[setCheckArguments(CustomMealListFragment(), Source.CUSTOM_MEAL)] = "Custom Meals"
    }

    private fun <M : MealItem, F> setCheckArguments(fragment: F, source: Source): F
            where F : BaseListFragment<M, *, *>, F : ICheckListenerOwner<M>, F : IItemClickListenerOwner<M> {

        //Configure checkbox module activation
        val bundle = Bundle()
        bundle.putItemActions(ItemAction.CHECK)
        bundle.putSource(source)

        fragment.arguments = bundle
        fragment.restoredItemPredicator = ::restoredItemPredicator
        fragment.onCheckListener = getCheckListener()
        fragment.onClickListener = getItemClickListener()

        return fragment
    }

    private fun <T : MealItem> restoredItemPredicator(item: T): Boolean {
        val originalItem = viewModel.pickedItems.firstOrNull {
            it.submissionId == item.submissionId
        }
        return if (originalItem != null) {
            item.amount = originalItem.amount
            item.carbs = originalItem.carbs
            true
        } else false
    }

    private fun <T : MealItem> getCheckListener(): ICheckListener<T> {
        return ICheckListener { item, isChecked, onChangeCallback ->
            viewModel.itemsChanged = true

            val existingItem = viewModel.pickedItems.firstOrNull {
                it.submissionId == item.submissionId
            }
            //Add checked item to list
            if (isChecked) {
                //Change button state if it's the first item
                if (viewModel.pickedItems.isEmpty()) {
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
                    baseCarbs = item.carbs,
                    baseAmount = item.unit.convert(viewModel.currentWeightUnits, item.amount),
                    startUnit = viewModel.currentWeightUnits
                ) { selectedAmount: Float, selectedCarbohydrates: Float, unit: WeightUnits ->
                    item.amount = selectedAmount
                    item.carbs = selectedCarbohydrates
                    item.unit = unit
                    viewModel.currentWeightUnits = unit
                    viewModel.pick(item)
                    onChangeCallback()
                }
            }
            //Remove checked item from list
            else {
                //Change button state if it's last item
                if (viewModel.pickedItems.size == 1) {
                    okButton.text = getString(R.string.cancel)
                }
                //Find item to remove based on submission id
                //This will avoid failed removals when
                //the object is not the same after restoring checked items
                viewModel.unPick(existingItem ?: item)
            }
        }
    }

    private fun <T : MealItem> getItemClickListener(): IItemClickListener<T> {
        return IItemClickListener { item, onChangeCallback ->
            val existingItem = viewModel.pickedItems.firstOrNull {
                it.submissionId == item.submissionId
            }
            if (existingItem != null) {
                MealAmountSelector(
                    ctx = requireContext(),
                    layoutInflater = layoutInflater,
                    baseCarbs = existingItem.carbs,
                    baseAmount = existingItem.unit.convert(
                        viewModel.currentWeightUnits,
                        existingItem.amount
                    ),
                    startUnit = viewModel.currentWeightUnits
                ) { selectedAmount: Float, selectedCarbohydrates: Float, unit: WeightUnits ->
                    item.amount = selectedAmount
                    item.carbs = selectedCarbohydrates
                    item.unit = unit
                    existingItem.amount = selectedAmount
                    existingItem.carbs = selectedCarbohydrates
                    existingItem.unit = unit
                    viewModel.currentWeightUnits = unit
                    onChangeCallback()
                }
            }
        }
    }
}