package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.addMeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.IngredientPickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IWeightUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IPickedFlexBoxRecycler
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putParentNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.IngredientPickViewModel


abstract class BaseAddMealFragment : BaseFragment(), IWeightUnitSpinner, IPickedFlexBoxRecycler {

    protected abstract val nestedNavigation: Navigation
    protected abstract val toAddMealsActionNestedNavigation: Navigation
    protected abstract val baskNestedActionNavigation: Navigation

    //Meals
    protected abstract val mealsRecyclerViewId: Int
    protected lateinit var ingredientsViewModel: IngredientPickViewModel
    protected val mealsRecyclerAdapter by lazy {
        IngredientPickRecyclerAdapter(ingredientsViewModel, requireContext())
    }

    override lateinit var currentWeightUnit: WeightUnits
    override lateinit var previousWeightUnit: WeightUnits

    protected abstract val weightUnitSpinnerId: Int
    protected lateinit var weightUnitSpinner: Spinner

    protected abstract val addIngredientsImgButtonId: Int
    protected lateinit var addIngredientsImgButton: ImageButton

    protected abstract val totalIngredientsWeightTextViewId: Int
    protected lateinit var totalIngredientsWeightTextView: TextView
    private var _currentIngredientQuantity: Float = 0.0F
    protected val currentIngredientsAmount get() = _currentIngredientQuantity

    protected abstract val totalIngredientsCarbohydratesTextViewId: Int
    protected lateinit var totalIngredientsCarbohydratesTextView: TextView
    private var _currentIngredientCarbohydrates: Int = 0
    protected val currentIngredientsCarbohydrates get() = _currentIngredientCarbohydrates

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelLazy: IngredientPickViewModel by navGraphViewModels(nestedNavigation.navId)
        ingredientsViewModel = viewModelLazy
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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

        if (ingredientsViewModel.ingredientsChanged) {
            ingredientsViewModel.ingredientsChanged = false
            mealsRecyclerAdapter.notifyDataSetChanged()
        }
    }

    private fun setupWeightUnitSpinner(view: View) {
        weightUnitSpinner = view.findViewById(weightUnitSpinnerId)
        super.setupWeightUnitSpinner(requireContext(), weightUnitSpinner)
    }

    private fun setupAddMealsBtn(view: View) {
        addIngredientsImgButton = view.findViewById(addIngredientsImgButtonId)
        addIngredientsImgButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putNavigation(baskNestedActionNavigation)
            bundle.putParentNavigation(nestedNavigation)
            view.findNavController().navigate(toAddMealsActionNestedNavigation.navId, bundle)
        }
    }

    private fun setupTotalIngredientsCarbsTextView(view: View) {
        totalIngredientsCarbohydratesTextView = view.findViewById(totalIngredientsCarbohydratesTextViewId)
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

    private fun countIngredientCarbohydrates(): Int =
        ingredientsViewModel.pickedItems.sumBy { ingredient ->
            ingredient.carbs
        }

    private fun countIngredientQuantity(): Float =
        ingredientsViewModel.pickedItems.fold(0.0F) { sum, ingredient ->
            sum + ingredient.unit.convert(currentWeightUnit, ingredient.amount)
        }

    protected open fun setupIngredients(view: View) {

        setupRecyclerView(
            this,
            ctx = requireContext(),
            view = view,
            recyclerViewId = mealsRecyclerViewId,
            adapter = mealsRecyclerAdapter,
            pickerViewModel = ingredientsViewModel
        ) {
            _currentIngredientCarbohydrates = countIngredientCarbohydrates()
            refreshIngredientCarbohydratesTextView()
            _currentIngredientQuantity = countIngredientQuantity()
            refreshIngredientQuantityTextView()
        }

        ingredientsViewModel.tryRestore()
    }

    override fun onWeightUnitChange(converter: (Float) -> Float) {
        _currentIngredientQuantity = converter(_currentIngredientQuantity)
        refreshIngredientQuantityTextView()
    }
}