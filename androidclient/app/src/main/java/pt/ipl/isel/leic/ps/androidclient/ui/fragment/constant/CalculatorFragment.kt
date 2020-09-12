package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.*
import androidx.navigation.navGraphViewModels
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.InsulinCalculator
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseAddMealFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRequiredTextInput
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IGlucoseUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CalculatorVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.closeKeyboard
import pt.ipl.isel.leic.ps.androidclient.ui.util.prompt.PromptConfirm
import pt.ipl.isel.leic.ps.androidclient.ui.util.putNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putParentNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_GLUCOSE_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CalculatorViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel

class CalculatorFragment : BaseAddMealFragment(), IRequiredTextInput, IGlucoseUnitSpinner {

    override lateinit var previousGlucoseUnit: GlucoseUnits
    override lateinit var currentGlucoseUnit: GlucoseUnits
    override val nestedNavigation = Navigation.SEND_TO_CALCULATOR
    override val toAddMealsActionNestedNavigation = Navigation.SEND_TO_PICK_CALCULATOR_INGREDIENTS
    override val backNestedActionNavigation = Navigation.BACK_TO_CALCULATOR_FROM_MEALS
    override val mealsRecyclerViewId: Int = R.id.calculator_meals_list
    override val weightUnitSpinnerId: Int = R.id.calculator_units_spinner
    override val addIngredientsImgButtonId = R.id.add_custom_meal_add_meal_ingredient
    override val totalIngredientsWeightTextViewId = R.id.total_ingredient_amount
    override val totalIngredientsCarbohydratesTextViewId = R.id.total_ingredient_carbs

    override val layout = R.layout.calculator_fragment
    override val vMProviderFactorySupplier = ::CalculatorVMProviderFactory
    private val viewModelProfiles: InsulinProfilesListViewModel
            by navGraphViewModels(Navigation.SEND_TO_CALCULATOR.navId) {
                vMProviderFactorySupplier(arguments, savedInstanceState, requireIntent())
            }
    private val viewModelCalculator by lazy {
        buildViewModel(savedInstanceState, CalculatorViewModel::class.java)
    }

    private var currentProfile: InsulinProfile? = null
    private val calculator = InsulinCalculator()
    private lateinit var bloodGlucoseEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var glucoseUnitsSpinner: Spinner
    private lateinit var profileStartTimeTextView: TextView
    private lateinit var profileEndTimeTextView: TextView
    private lateinit var profileGlucoseObjectiveTextView: TextView
    private lateinit var profileInsulinSensitivityTextView: TextView
    private lateinit var profileCarbohydrateRatioTextView: TextView
    private lateinit var currentProfileTitleTextView: TextView
    private lateinit var currentProfileLayout: RelativeLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bloodGlucoseEditText = view.findViewById(R.id.user_blood_glucose)
        calculateButton = view.findViewById(R.id.calculate_button)
        glucoseUnitsSpinner = view.findViewById(R.id.calculator_glucose_units)
        profileStartTimeTextView = view.findViewById(R.id.start_time_value)
        profileEndTimeTextView = view.findViewById(R.id.end_time_value)
        profileGlucoseObjectiveTextView = view.findViewById(R.id.glucose_objective_value)
        profileInsulinSensitivityTextView = view.findViewById(R.id.insulin_sensitivity_factor_value)
        profileCarbohydrateRatioTextView = view.findViewById(R.id.carbohydrate_ratio_value)
        currentProfileTitleTextView = view.findViewById(R.id.current_profile_label)
        currentProfileLayout = view.findViewById(R.id.current_profile_rl)

        setupGlucoseUnitSpinner(
            requireContext(),
            glucoseUnitsSpinner,
            viewModelCalculator.currentGlucoseUnit
        )

        setupInsulinProfilesViewModel()
    }

    override fun setupIngredients(view: View) {
        super.setupIngredients(view)

        val argumentMeal = viewModelProfiles.argumentMeal
        if (argumentMeal != null) {
            viewModelProfiles.argumentMeal = null
            viewModel.pick(argumentMeal)
        }
    }

    /**
     * Observes modifications in the LiveData
     */
    private fun setupInsulinProfilesViewModel() {
        viewModelProfiles.onError = {
            log.e(it)
            Toast.makeText(
                app,
                R.string.profiles_error,
                Toast.LENGTH_LONG
            ).show()
        }
        viewModelProfiles.observe(this) { profilesList ->
            showCurrentProfileDetails(profilesList)
        }
        viewModelProfiles.setupList()
        setupCalculateButton()
    }

    override fun setupWeightUnitSpinner(view: View) {
        weightUnitSpinner = view.findViewById(weightUnitSpinnerId)
        setupWeightUnitSpinner(
            requireContext(),
            weightUnitSpinner,
            viewModelCalculator.currentWeightUnit
        )
    }

    /**
     * Searches for a profile that matches the current time
     */
    private fun getCurrentProfile(profilesList: List<InsulinProfile>): InsulinProfile? {
        return profilesList.find(InsulinProfile::isActive)
    }

    /**
     * Shows the profile details, according to the current time
     */
    private fun showCurrentProfileDetails(profilesList: List<InsulinProfile>) {
        currentProfile = getCurrentProfile(profilesList)
        val profile = currentProfile

        if (profile == null) {
            currentProfileTitleTextView.text = getString(R.string.setup_a_profile_before_proceed)
            currentProfileLayout.visibility = View.GONE
            return
        }

        currentProfileTitleTextView.text = getString(R.string.your_current_profile_for_this_time)
        currentProfileLayout.visibility = View.VISIBLE

        profileStartTimeTextView.text = profile.startTime
        profileEndTimeTextView.text = profile.endTime
        profileGlucoseObjectiveTextView.text = profile.glucoseObjective.toString()
        profileInsulinSensitivityTextView.text = String.format(
            "%1\$.2f / %2\$s",
            profile.glucoseAmountPerInsulin,
            resources.getString(R.string.insulin_unit)
        )
        profileCarbohydrateRatioTextView.text = String.format(
            "%1\$.2f / %2\$s",
            profile.carbsAmountPerInsulin,
            resources.getString(R.string.insulin_unit)
        )
    }

    /**
     * Sets up the calculation button, calculating the insulin to be injected following
     * the previously configured profile.
     */
    private fun setupCalculateButton() {

        calculateButton.setOnClickListener {

            //Update current profile (might have expired since fragment setup)
            showCurrentProfileDetails(profilesList = viewModelProfiles.items)

            if (currentProfile == null) {
                PromptConfirm(
                    requireContext(),
                    R.string.add_a_profile_dialog,
                    R.string.prompt_add_profile
                ) {
                    val bundle = Bundle()
                    bundle.putNavigation(Navigation.BACK_TO_CALCULATOR_FROM_ADD_PROFILE)
                    bundle.putParentNavigation(Navigation.SEND_TO_CALCULATOR)
                    super.navigate(Navigation.SEND_TO_ADD_INSULIN_PROFILE_FROM_CALCULATOR, bundle)
                }.show()
                return@setOnClickListener
            }

            if (!inputValid()) {
                return@setOnClickListener
            }

            var currentBloodGlucoseValue = bloodGlucoseEditText.text.toString().toFloat()

            //Convert current glucose value to default unit
            currentBloodGlucoseValue = currentGlucoseUnit.convert(
                targetUnit = DEFAULT_GLUCOSE_UNIT,
                value = currentBloodGlucoseValue
            )

            val result = calculator.calculateMealInsulin(
                currentProfile!!,
                currentBloodGlucoseValue,
                currentIngredientsCarbohydrates
            )

            closeKeyboard(this.requireActivity())
            showResultDialog(result)

            // Clean fields after show result
            bloodGlucoseEditText.text = null
            bloodGlucoseEditText.clearFocus()
        }
    }

    private fun inputValid(): Boolean {
        if (!validateTextViews(requireContext(), bloodGlucoseEditText)) {
            return false
        }
        if (viewModel.pickedItems.isEmpty()) {
            Toast.makeText(
                requireContext(),
                R.string.calculator_ingredients_required,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    /**
     * Shows the result dialog window
     */
    private fun showResultDialog(result: Float) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.results_title_dialog))
            .setMessage(String.format(getString(R.string.calculator_result), result))
            .setPositiveButton(view?.context?.getString(R.string.ok)) { _, _ -> }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        if (viewModelProfiles.itemsChanged) {
            viewModelProfiles.itemsChanged = false
            showCurrentProfileDetails(viewModelProfiles.items)
        }
    }

    override fun onGlucoseUnitChange(converter: (Float) -> Float) {
        if (bloodGlucoseEditText.text.isNotBlank()) {
            val currentValue = bloodGlucoseEditText.text.toString().toFloat()
            bloodGlucoseEditText.setText(converter(currentValue).toString())
        }
        viewModelCalculator.currentGlucoseUnit = currentGlucoseUnit
    }

    override fun onWeightUnitChange(converter: (Float) -> Float) {
        super.onWeightUnitChange(converter)
        viewModelCalculator.currentWeightUnit = currentWeightUnit
    }

    override fun getViewModels(): Iterable<Parcelable> = super.getViewModels()
        .plus(listOf(viewModelProfiles, viewModelCalculator))
}