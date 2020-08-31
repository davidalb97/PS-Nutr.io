package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.addMeal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.CuisinePickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick.CuisinePickSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IRemainingPickSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddCustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getDbId
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddCustomMealViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel


class AddCustomMealFragment : BaseAddMealFragment(), IRemainingPickSpinner {

    //All data
    private lateinit var viewModel: AddCustomMealViewModel

    //Cuisines
    private val cuisinesRecyclerViewId: Int = R.id.custom_meal_cuisines_list
    private val cuisinesSpinnerId: Int = R.id.custom_meal_cuisines_spinner
    private lateinit var cuisinesViewModel: CuisinePickViewModel
    private val cuisinesSpinnerAdapter by lazy {
        CuisinePickSpinnerAdapter(cuisinesViewModel, requireContext())
    }
    private val cuisinesRecyclerAdapter by lazy {
        CuisinePickRecyclerAdapter(cuisinesViewModel, requireContext())
    }

    private lateinit var mMealNameEditText: EditText
    private lateinit var additionalAmountEditText: EditText
    private lateinit var imageUrlEditText: EditText
    private lateinit var submitButton: Button

    override val nestedNavigation: Navigation = Navigation.SEND_TO_ADD_CUSTOM_MEAL
    override val toAddMealsActionNestedNavigation = Navigation.SEND_TO_PICK_CUSTOM_MEAL_INGREDIENTS
    override val baskNestedActionNavigation = Navigation.BACK_TO_CUSTOM_MEAL
    override val mealsRecyclerViewId: Int = R.id.custom_meal_meals_list
    override val weightUnitSpinnerId: Int = R.id.custom_meal_units_spinner
    override val addIngredientsImgButtonId: Int = R.id.add_custom_meal_add_meal_ingredient
    override val totalIngredientsWeightTextViewId: Int = R.id.total_ingredient_amount
    override val totalIngredientsCarbohydratesTextViewId: Int = R.id.total_ingredient_carbs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, AddCustomMealViewModel::class.java)
        cuisinesViewModel = buildViewModel(savedInstanceState, CuisinePickViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCuisines(view)
        setupSubmit(view)

        mMealNameEditText = view.findViewById(R.id.meal_name)
        mMealNameEditText.setText(viewModel.editMeal?.name)

        additionalAmountEditText = view.findViewById(R.id.meal_portion_quantity)
        additionalAmountEditText.setText(viewModel.editMeal?.amount?.toString())

        imageUrlEditText = view.findViewById(R.id.custom_meal_image_url)
        imageUrlEditText.setText(viewModel.editMeal?.imageUri?.toString())
    }

    override fun setupIngredients(view: View) {
        super.setupIngredients(view)

        val editMeal = viewModel.editMeal
        if (mealsViewModel.items.isEmpty() && editMeal != null) {
            val ingredients = editMeal.mealComponents.plus(editMeal.ingredientComponents)
            if (ingredients.isNotEmpty()) {
                mealsViewModel.pickedLiveDataHandler.set(ingredients)
            }
        }
    }

    private fun setupCuisines(view: View) {
        setupRecyclerView(
            this,
            ctx = requireContext(),
            view = view,
            recyclerViewId = cuisinesRecyclerViewId,
            adapter = cuisinesRecyclerAdapter,
            pickerViewModel = cuisinesViewModel
        )
        setupSpinner(
            this,
            view = view,
            spinnerId = cuisinesSpinnerId,
            adapter = cuisinesSpinnerAdapter,
            pickerViewModel = cuisinesViewModel
        )
        when {
            viewModel.editMeal?.cuisines != null -> {
                val cuisines = viewModel.editMeal!!.cuisines
                cuisinesViewModel.tryRestore()
                cuisinesViewModel.pickedLiveDataHandler.add(cuisines)
            }
            else -> cuisinesViewModel.update()
        }
    }

    private fun setupSubmit(view: View) {
        submitButton = view.findViewById(R.id.create_custom_meal_button)
        submitButton.setOnClickListener {
            val noneFieldBlank =
                listOf(
                    mMealNameEditText,
                    additionalAmountEditText
                ).none { it.text.isBlank() }

            val noneEmpty = listOf(
                cuisinesViewModel,
                mealsViewModel
            ).none { it.pickedItems.isEmpty() }

            if (noneFieldBlank && noneEmpty) {
                val customMeal = getCurrentCustomMeal()
                val editMeal = viewModel.editMeal
                if (editMeal == null) {
                    viewModel.addCustomMeal(customMeal = customMeal, error = log::e)
                } else {
                    viewModel.editCustomMeal(
                        submission = requireNotNull(editMeal.submissionId),
                        customMeal = customMeal,
                        error = log::e
                    )
                }
            }
        }
    }

    private fun getAdditionalAmount(): Float {
        return additionalAmountEditText.text?.toString()?.toFloat() ?: 0.0F
    }

    private fun getCurrentCustomMeal(): CustomMeal {
        return CustomMeal(
            dbId = viewModel.editMeal?.dbId,
            submissionId = viewModel.editMeal?.submissionId,
            name = mMealNameEditText.text.toString(),
            carbs = currentIngredientsCarbohydrates,
            amount = currentWeightUnit.convert(
                DEFAULT_WEIGHT_UNIT,
                currentIngredientsAmount + getAdditionalAmount()
            ),
            unit = DEFAULT_WEIGHT_UNIT,
            imageUri = imageUrlEditText.text?.toString()?.let { Uri.parse(it) },
            ingredientComponents = mealsViewModel.pickedItems
                .map(::toMealIngredient)
                .filter { !it.isMeal },
            mealComponents = mealsViewModel.pickedItems
                .map(::toMealIngredient)
                .filter { it.isMeal },
            cuisines = cuisinesViewModel.pickedItems
        )
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

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): AddCustomMealRecyclerVMProviderFactory {
        return AddCustomMealRecyclerVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getLayout() = R.layout.add_custom_meal
}