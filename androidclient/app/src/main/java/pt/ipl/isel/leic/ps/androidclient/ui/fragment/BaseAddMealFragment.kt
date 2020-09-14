package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.MealItemPickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IPickedFlexBoxRecycler
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IWeightUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putParentNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.MealItemPickViewModel


abstract class BaseAddMealFragment : BaseViewModelFragment<MealItemPickViewModel>(),
    IWeightUnitSpinner,
    IPickedFlexBoxRecycler {

    protected abstract val nestedNavigation: Navigation
    protected abstract val toAddMealsActionNestedNavigation: Navigation
    protected abstract val backNestedActionNavigation: Navigation

    //Meals
    override val viewModel: MealItemPickViewModel by lazy {
        val viewModelLazy: MealItemPickViewModel by navGraphViewModels(nestedNavigation.navId) {
            vMProviderFactorySupplier(arguments, savedInstanceState, requireActivity().intent)
        }
        viewModelLazy
    }
    override val vmClass = MealItemPickViewModel::class.java
    protected abstract val mealsRecyclerViewId: Int
    private val mealsRecyclerAdapter by lazy {
        MealItemPickRecyclerAdapter(viewModel, requireContext())
    }

    override lateinit var currentWeightUnit: WeightUnits
    override lateinit var previousWeightUnit: WeightUnits

    abstract override val weightUnitSpinnerId: Int
    override lateinit var weightUnitSpinner: Spinner

    protected abstract val addIngredientsImgButtonId: Int
    private lateinit var addIngredientsImgButton: ImageButton

    protected abstract val totalIngredientsWeightTextViewId: Int
    private lateinit var totalIngredientsWeightTextView: TextView
    private var _currentIngredientQuantity: Float = 0.0F
    protected val currentIngredientsAmount get() = _currentIngredientQuantity

    protected abstract val totalIngredientsCarbohydratesTextViewId: Int
    private lateinit var totalIngredientsCarbohydratesTextView: TextView
    private var _currentIngredientCarbohydrates: Float = 0.0F
    protected val currentIngredientsCarbohydrates get() = _currentIngredientCarbohydrates

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddMealsBtn(view)
        setupWeightUnitSpinner(view)
        setupTotalIngredientsCarbsTextView(view)
        setupTotalIngredientsWeightTextView(view)
        setupIngredients(view)
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.itemsChanged) {
            viewModel.itemsChanged = false
            mealsRecyclerAdapter.notifyDataSetChanged()
        }
    }

    protected open fun setupWeightUnitSpinner(view: View) {
        setupWeightUnitSpinner(
            view = view,
            context = requireContext()
        )
    }

    private fun setupAddMealsBtn(view: View) {
        addIngredientsImgButton = view.findViewById(addIngredientsImgButtonId)
        addIngredientsImgButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putNavigation(backNestedActionNavigation)
            bundle.putParentNavigation(nestedNavigation)
            view.findNavController().navigate(toAddMealsActionNestedNavigation.navId, bundle)
        }
    }

    private fun setupTotalIngredientsCarbsTextView(view: View) {
        totalIngredientsCarbohydratesTextView =
            view.findViewById(totalIngredientsCarbohydratesTextViewId)
        _currentIngredientCarbohydrates = countIngredientCarbohydrates()
        refreshIngredientCarbohydratesTextView()
    }

    private fun refreshIngredientCarbohydratesTextView() {
        totalIngredientsCarbohydratesTextView.text = String.format(
            getString(R.string.ingredient_carbohydrates_amount),
            _currentIngredientCarbohydrates,
            DEFAULT_WEIGHT_UNIT.toString()
        )
    }

    private fun setupTotalIngredientsWeightTextView(view: View) {
        totalIngredientsWeightTextView = view.findViewById(totalIngredientsWeightTextViewId)
        _currentIngredientQuantity = countIngredientQuantity()
        refreshIngredientQuantityTextView()
    }

    private fun refreshIngredientQuantityTextView() {
        totalIngredientsWeightTextView.text = String.format(
            getString(R.string.ingredient_weight),
            _currentIngredientQuantity,
            currentWeightUnit.toString()
        )
    }

    private fun countIngredientCarbohydrates(): Float =
        viewModel.pickedItems.fold(0.0F) { sum, ingredient ->
            sum + ingredient.carbs
        }

    protected fun countIngredientQuantity(
        targetUnit: WeightUnits,
        ingredients: Iterable<MealItem>
    ): Float {
        return ingredients.fold(0.0F) { sum, ingredient ->
            sum + ingredient.unit.convert(targetUnit, ingredient.amount)
        }
    }

    protected fun countIngredientQuantity(): Float =
        countIngredientQuantity(currentWeightUnit, viewModel.pickedItems)

    protected open fun setupIngredients(view: View) {

        setupRecyclerView(
            this,
            ctx = requireContext(),
            view = view,
            recyclerViewId = mealsRecyclerViewId,
            adapter = mealsRecyclerAdapter,
            pickerViewModel = viewModel
        ) {
            _currentIngredientCarbohydrates = countIngredientCarbohydrates()
            refreshIngredientCarbohydratesTextView()
            _currentIngredientQuantity = countIngredientQuantity()
            refreshIngredientQuantityTextView()
        }

        viewModel.tryRestore()
    }

    override fun onWeightUnitChange(converter: (Float) -> Float) {
        _currentIngredientQuantity = converter(_currentIngredientQuantity)
        refreshIngredientQuantityTextView()
    }

    override fun getViewModels(): Iterable<Parcelable> = listOf(viewModel)
}