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
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel
import java.text.SimpleDateFormat
import java.util.*

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
        setupGlucoseUnitSpinner(requireContext(), glucoseUnitsSpinner)

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
            if (viewModelProfiles.items.isEmpty()) {
                showNoExistingProfilesPrompt()
            }
            setupCurrentProfile()
        }
        viewModelProfiles.setupList()
        setupCalculateButton()
    }

    private fun setupCurrentProfile() {
        this.currentProfile = getCurrentProfile()?.also { currentProfile ->
            showCurrentProfileDetails(currentProfile)
        }
    }

    /**
     * Pops up when there are no insulin profiles created
     */
    private fun showNoExistingProfilesPrompt() {
        Toast.makeText(
            app,
            R.string.setup_a_profile_before_proceed,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Searches for a profile that matches the current time
     */
    private fun getCurrentProfile(): InsulinProfile? {

        if (viewModelProfiles.items.isEmpty()) {
            showNoExistingProfilesPrompt()
            return null
        }

        // Gets actual time in hh:mm format
        val timeInstance = SimpleDateFormat("HH:mm")
        val calendar = Calendar.getInstance()
        val currentTime = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
        val now = timeInstance.parse(currentTime)!!

        return viewModelProfiles.items.find { savedProfile ->
            val parsedSavedStartTime = timeInstance.parse(savedProfile.startTime)
            val parsedSavedEndTime = timeInstance.parse(savedProfile.endTime)
            now.after(parsedSavedStartTime) && now.before(parsedSavedEndTime)
        }
    }

    /**
     * Shows the profile details, according to the current time
     */
    private fun showCurrentProfileDetails(profile: InsulinProfile) {

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
            setupCurrentProfile()

            if(currentProfile == null) {
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
        if (currentProfile == null) {
            showNoExistingProfilesPrompt()
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

    override fun onGlucoseUnitChange(converter: (Float) -> Float) {
        if (bloodGlucoseEditText.text.isNotBlank()) {
            val currentValue = bloodGlucoseEditText.text.toString().toFloat()
            bloodGlucoseEditText.setText(converter(currentValue).toString())
        }
    }

    override fun getViewModels(): Iterable<Parcelable> = super.getViewModels()
        .plus(viewModelProfiles)
}