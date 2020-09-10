package pt.ipl.isel.leic.ps.androidclient.ui.fragment.create

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.CuisinePickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick.CuisinePickSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseAddMealFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRequiredTextInput
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IRemainingPickSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddCustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getDbId
import pt.ipl.isel.leic.ps.androidclient.ui.util.prompt.MealAmountSelector
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.AddCustomMealViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel


class AddCustomMealFragment : BaseAddMealFragment(), IRemainingPickSpinner, IRequiredTextInput {

    //All data
    override val layout = R.layout.add_custom_meal
    override val vMProviderFactorySupplier = ::AddCustomMealRecyclerVMProviderFactory
    private val addViewModel by lazy {
        buildViewModel(savedInstanceState, AddCustomMealViewModel::class.java)
    }

    //Cuisines
    private val cuisinesRecyclerViewId: Int = R.id.custom_meal_cuisines_list
    private val cuisinesSpinnerId: Int = R.id.custom_meal_cuisines_spinner
    private val cuisinesViewModel by lazy {
        buildViewModel(savedInstanceState, CuisinePickViewModel::class.java)
    }
    private val cuisinesSpinnerAdapter by lazy {
        CuisinePickSpinnerAdapter(cuisinesViewModel, requireContext())
    }
    private val cuisinesRecyclerAdapter by lazy {
        CuisinePickRecyclerAdapter(cuisinesViewModel, requireContext())
    }

    private lateinit var mMealNameEditText: EditText
    private lateinit var additionalAmountTextView: TextView
    private lateinit var addAdditionalAmountTextView: TextView
    private lateinit var addAdditionalAmountImageButton: ImageButton
    private lateinit var imageUrlEditText: EditText
    private lateinit var submitButton: Button

    override val nestedNavigation: Navigation = Navigation.SEND_TO_ADD_CUSTOM_MEAL
    override val toAddMealsActionNestedNavigation = Navigation.SEND_TO_PICK_CUSTOM_MEAL_INGREDIENTS
    override val backNestedActionNavigation = Navigation.BACK_TO_CUSTOM_MEAL
    override val mealsRecyclerViewId: Int = R.id.custom_meal_meals_list
    override val weightUnitSpinnerId: Int = R.id.custom_meal_units_spinner
    override val addIngredientsImgButtonId: Int = R.id.add_custom_meal_add_meal_ingredient
    override val totalIngredientsWeightTextViewId: Int = R.id.total_ingredient_amount
    override val totalIngredientsCarbohydratesTextViewId: Int = R.id.total_ingredient_carbs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //If it is editing meal and it is not the first time loading the fragment
        if (addViewModel.mealItem != null && addViewModel.mealInfo == null) {
            addViewModel.observeInfo(this) { editMeal ->
                addViewModel.removeObservers(this)

                //Restore name
                addViewModel.currentName = editMeal.name

                //Restore unit
                addViewModel.currentWeightUnits = editMeal.unit

                //Restore image
                addViewModel.currentImg = editMeal.imageUri?.toString()

                //Restore ingredients
                val ingredients = editMeal.ingredientComponents.plus(editMeal.mealComponents)
                viewModel.pickedLiveDataHandler.restoreFromValues(ingredients)

                //Restore additional meal amount
                addViewModel.currentAdditionalAmount = editMeal.amount - countIngredientQuantity(
                    editMeal.unit,
                    ingredients
                )

                //Restore cuisines
                cuisinesViewModel.pickedLiveDataHandler.restoreFromValues(editMeal.cuisines)

                setupView(view, savedInstanceState)
            }
            addViewModel.setupList()
        } else setupView(view, savedInstanceState)
    }

    private fun setupView(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCuisines(view)
        setupSubmit(view)

        mMealNameEditText = view.findViewById(R.id.meal_name)
        mMealNameEditText.setText(addViewModel.currentName)

        setupAdditionalAmount(view)

        imageUrlEditText = view.findViewById(R.id.custom_meal_image_url)
        imageUrlEditText.setText(addViewModel.currentImg)
    }

    private fun setupAdditionalAmount(view: View) {
        additionalAmountTextView = view.findViewById(R.id.custom_meal_additional_amount)
        addAdditionalAmountTextView = view.findViewById(R.id.add_custom_meal_add_amount_txt)
        addAdditionalAmountImageButton = view.findViewById(R.id.add_custom_meal_add_amount_btn)
        addAdditionalAmountImageButton.setOnClickListener {
            MealAmountSelector(
                ctx = requireContext(),
                layoutInflater = layoutInflater,
                baseCarbs = 0.0F,
                baseAmountGrams = currentWeightUnit.convert(
                    WeightUnits.GRAMS,
                    addViewModel.currentAdditionalAmount
                ),
                mealUnit = currentWeightUnit,
                hideCarbs = true
            ) { amountGrams: Float, _: Float ->
                addViewModel.currentAdditionalAmount = DEFAULT_WEIGHT_UNIT.convert(
                    targetUnit = currentWeightUnit,
                    value = amountGrams
                )
                refreshAdditionalAmount()
            }
        }
        refreshAdditionalAmount()
    }

    private fun refreshAdditionalAmount() {
        additionalAmountTextView.text = String.format(
            getString(R.string.custom_meal_additional_quantity),
            addViewModel.currentAdditionalAmount
        )
        addAdditionalAmountTextView.text = getString(
            if (addViewModel.currentAdditionalAmount != 0.0F)
                R.string.edit_amount
            else R.string.add_amount
        )
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
        cuisinesViewModel.setupList()
    }

    private fun setupSubmit(view: View) {
        submitButton = view.findViewById(R.id.create_custom_meal_button)
        submitButton.setOnClickListener {
            if (!isInputValid()) {
                return@setOnClickListener
            }
            val customMeal = getCurrentCustomMeal()
            val editMeal = addViewModel.mealItem
            if (editMeal == null) {
                addViewModel.addCustomMeal(
                    customMeal = customMeal,
                    error = ::onSubmitError,
                    success = ::onCreateSuccess
                )
            } else {
                addViewModel.editCustomMeal(
                    submission = requireNotNull(editMeal.submissionId),
                    customMeal = customMeal,
                    error = ::onSubmitError,
                    success = ::onEditSuccess
                )
            }
        }
    }

    private fun onCreateSuccess() {
        Toast.makeText(app, getString(R.string.created_custom_meal), Toast.LENGTH_SHORT).show()
        popUpBackStack()
    }

    private fun onEditSuccess() {
        Toast.makeText(app, getString(R.string.edited_custom_meal), Toast.LENGTH_SHORT).show()
        popUpBackStack()
    }

    private fun onSubmitError(throwable: Throwable) {
        log.e(throwable)
        Toast.makeText(app, getString(R.string.submit_fail_custom_meal), Toast.LENGTH_SHORT).show()
        popUpBackStack()
    }

    private fun isInputValid(): Boolean {
        if (!validateTextViews(requireContext(), mMealNameEditText)) {
            return false
        }
        if (cuisinesViewModel.pickedItems.isEmpty()) {
            Toast.makeText(
                app,
                getString(R.string.select_cuisine),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (viewModel.pickedItems.isEmpty()) {
            Toast.makeText(
                app,
                getString(R.string.select_ingredients),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun getCurrentCustomMeal(): CustomMeal {
        val ingredients = viewModel.pickedItems.map(::toMealIngredient)
        return CustomMeal(
            dbId = addViewModel.mealItem?.dbId,
            submissionId = addViewModel.mealItem?.submissionId,
            name = mMealNameEditText.text.toString(),
            carbs = currentIngredientsCarbohydrates,
            amount = currentWeightUnit.convert(
                DEFAULT_WEIGHT_UNIT,
                currentIngredientsAmount + addViewModel.currentAdditionalAmount
            ),
            unit = DEFAULT_WEIGHT_UNIT,
            imageUri = imageUrlEditText.text?.toString()?.let(Uri::parse),
            ingredientComponents = ingredients.filter { !it.isMeal },
            mealComponents = ingredients.filter { it.isMeal },
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
            favorites = mealItem.favorites,
            unit = mealItem.unit,
            imageUri = mealItem.imageUri,
            source = mealItem.source
        )
    }

    override fun onWeightUnitChange(converter: (Float) -> Float) {
        super.onWeightUnitChange(converter)
        addViewModel.currentWeightUnits = currentWeightUnit
        addViewModel.currentAdditionalAmount = converter(addViewModel.currentAdditionalAmount)
        refreshAdditionalAmount()
    }

    override fun getViewModels(): Iterable<Parcelable> = super.getViewModels()
        .plus(listOf(addViewModel, cuisinesViewModel))
}