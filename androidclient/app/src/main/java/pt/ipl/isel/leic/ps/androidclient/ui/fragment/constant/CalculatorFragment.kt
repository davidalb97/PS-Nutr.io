package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.InsulinCalculator
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CalculatorVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.MealInfoViewModel
import java.text.SimpleDateFormat
import java.util.*

class CalculatorFragment : BaseFragment() {

    private lateinit var viewModelProfiles: InsulinProfilesListViewModel
    private lateinit var viewModelMeal: MealInfoViewModel
    private var receivedMeal: MealInfo? = null
    private var currentProfile: InsulinProfile? = null
    private val calculator = InsulinCalculator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelProfiles =
            buildViewModel(savedInstanceState, InsulinProfilesListViewModel::class.java)
        viewModelMeal = buildViewModel(savedInstanceState, MealInfoViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerSetup()
        searchForBundledMeal()

        viewModelProfiles.onError = {
            log.e(it)
            Toast.makeText(app, R.string.profiles_error, Toast.LENGTH_LONG)
                .show()
        }

        observeExistingInsulinProfiles()
        setupAddMealButtonListener()
        viewModelProfiles.update()
    }

    /**
     * Setups the measurement units spinner following the shared preferences.
     */
    @Suppress("UNCHECKED_CAST")
    private fun spinnerSetup() {
        val spinner = view?.findViewById<Spinner>(R.id.measurement_units)
        val spinnerAdapter: ArrayAdapter<String> = spinner!!.adapter as ArrayAdapter<String>

        val defaultUnitKey = sharedPreferences.getDefaultUnitKey()

        val spinnerPosition = spinnerAdapter.getPosition(defaultUnitKey)
        spinner.setSelection(spinnerPosition)

        // Changes the blood glucose value according to the unit spinner
        val currentBloodGlucose = view?.findViewById<EditText>(R.id.user_blood_glucose)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                throw NotImplementedError("This function is not implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (currentBloodGlucose!!.text.isNotBlank()) {
                    val newUnit = GlucoseUnits.valueOf(spinner.selectedItem.toString())
                    val oldUnit = when (newUnit) {
                        GlucoseUnits.MILLI_GRAM_PER_DL -> GlucoseUnits.MILLI_MOL_PER_L
                        GlucoseUnits.MILLI_MOL_PER_L -> GlucoseUnits.MILLI_GRAM_PER_DL
                    }
                    val currentValue = currentBloodGlucose.text.toString().toDouble()
                    currentBloodGlucose.setText(oldUnit.convert(newUnit, currentValue).toString())
                }
            }
        }
    }

    /**
     * Checks if the user already selected a meal for the calculation.
     * This meal will be present inside the fragment bundle if it was
     * already selected.
     */
    private fun searchForBundledMeal() {
        if (viewModelMeal.mealInfo != null) {
            addBundledMealHolder(viewModelMeal.mealInfo!!)
        } else if (viewModelMeal.mealItem != null) {
            val owner = this
            viewModelMeal.observeInfo(owner) {
                addBundledMealHolder(it)
                viewModelMeal.removeObservers(owner)
            }
            viewModelMeal.onError = log::e
            viewModelMeal.update()
        }
    }

    /**
     * Setups the CardView in the fragment that will hold the chosen meal.
     */
    private fun addBundledMealHolder(mealInfo: MealInfo) {
        this.receivedMeal = mealInfo
        val selectedMealCard = view?.findViewById<CardView>(R.id.calc_meal_card)
        val selectedMealName = view?.findViewById<TextView>(R.id.calc_meal_name)
        val selectedMealDelBtn = view?.findViewById<ImageButton>(R.id.remove_meal_from_calc_button)

        selectedMealName?.text = receivedMeal?.name
        selectedMealCard?.visibility = View.VISIBLE

        selectedMealDelBtn?.setOnClickListener {
            cleanBundledMeal(selectedMealCard)
        }
    }

    /**
     * Setups the button that allows the user to select meals.
     */
    private fun setupAddMealButtonListener() {
        val addButton = view?.findViewById<ImageButton>(R.id.meal_add_button)
        addButton?.setOnClickListener {
            val bundle = Bundle()
            bundle.putNavigation(Navigation.SEND_TO_CALCULATOR)
            view?.findNavController()?.navigate(R.id.nav_add_meal_to_calculator, bundle)
        }
    }

    /**
     * Observes modifications in the LiveData
     */
    private fun observeExistingInsulinProfiles() {
        viewModelProfiles.observe(this) { profilesList ->
            if (profilesList.isEmpty())
                showNoExistingProfilesToast()
            else {
                getProfile()
                setupCalculateButton()
            }
        }
    }

    /**
     * Pops up when there are no insulin profiles created
     */
    private fun showNoExistingProfilesToast() {
        Toast.makeText(
            app,
            R.string.setup_a_profile_before_proceed,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Gets a valid profile based on the current LocalTime;
     * Gets the bundled meal, saved in this fragment field;
     * Calculates the insulin dosage that needs to be injected.
     */
    private fun getProfile() {
        currentProfile = getActualProfile()
        if (currentProfile != null) {
            showActualProfileDetails(currentProfile!!)
        }
    }

    /**
     * Searches for a profile that matches the current time
     */
    private fun getActualProfile(): InsulinProfile? {

        // Gets actual time in hh:mm format
        val calendar = Calendar.getInstance()
        val currentTime =
            "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

        val profiles = viewModelProfiles.items
        if (profiles.isEmpty()) {
            showNoExistingProfilesToast()
            return null
        }

        val timeInstance = SimpleDateFormat("HH:mm")
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
    private fun showActualProfileDetails(profile: InsulinProfile) {
        val startTime = view?.findViewById<TextView>(R.id.start_time_value)
        val endTime = view?.findViewById<TextView>(R.id.end_time_value)
        val glucoseObjective = view?.findViewById<TextView>(R.id.glucose_objective_value)
        val insulinSensitivity =
            view?.findViewById<TextView>(R.id.insulin_sensitivity_factor_value)
        val carbohydrates = view?.findViewById<TextView>(R.id.carbohydrate_ratio_value)

        startTime?.text = profile.startTime
        endTime?.text = profile.endTime
        glucoseObjective?.text = profile.glucoseObjective.toString()
        insulinSensitivity?.text = String.format(
            "%1\$d / %2\$s",
            profile.glucoseAmountPerInsulin,
            resources.getString(R.string.insulin_unit)
        )
        carbohydrates?.text = String.format(
            "%1\$d / %2\$s",
            profile.carbsAmountPerInsulin,
            resources.getString(R.string.insulin_unit)
        )
    }

    /**
     * Sets up the calculation button, calculating the insulin to be injected following
     * the previously configured profile.
     */
    private fun setupCalculateButton() {
        val currentBloodGlucose =
            view?.findViewById<EditText>(R.id.user_blood_glucose)
        val calculateButton =
            view?.findViewById<Button>(R.id.calculate_button)
        val spinner = view?.findViewById<Spinner>(R.id.measurement_units)

        calculateButton?.setOnClickListener {
            val result: Float
            if (receivedMeal != null && currentProfile != null) {
                // Current blood glucose value
                var bloodGlucoseValue = currentBloodGlucose!!
                    .text.toString().toDouble().toInt()

                // If the unit is different from default, convert back to the value to pass to
                // the calculator
                val currentUnit = GlucoseUnits.valueOf(spinner?.selectedItem.toString())
                if (currentUnit != DEFAULT_GLUCOSE_UNIT) {
                    bloodGlucoseValue = currentUnit.convert(
                        DEFAULT_GLUCOSE_UNIT,
                        currentBloodGlucose.text.toString().toDouble()
                    ).toInt()
                }

                result =
                    calculator.calculateMealInsulin(
                        currentProfile!!,
                        bloodGlucoseValue,
                        receivedMeal!!.carbs
                    )

                closeKeyboard(this.requireActivity())
                showResultDialog(result)
                // Clean fields after show result
                cleanCurrentGlucose(currentBloodGlucose)
                cleanBundledMeal()
            }
        }
    }

    /**
     * Shows the result dialog window
     */
    private fun showResultDialog(result: Float) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.results_title_dialog))
        builder.setMessage("You need to inject $result insulin doses")
        builder.setPositiveButton(view?.context?.getString(R.string.Dialog_Ok)) { _, _ -> }
        builder.create().show()
    }

    /**
     * Cleans the current blood glucose text field
     */
    private fun cleanCurrentGlucose(textBox: EditText? = null) {
        if (textBox != null) {
            textBox.text = null
            textBox.clearFocus()
        } else {
            val currentBloodGlucose =
                view?.findViewById<EditText>(R.id.user_blood_glucose)
            currentBloodGlucose?.text = null
            currentBloodGlucose?.clearFocus()
        }
    }

    /**
     * Cleans the bundled meal and the fragment meal holder
     */
    private fun cleanBundledMeal(card: CardView? = null) {
        if (card != null) {
            card.visibility = View.INVISIBLE
        } else {
            val selectedMealCard: CardView? =
                view?.findViewById(R.id.calc_meal_card)
            selectedMealCard?.visibility = View.INVISIBLE
        }
        receivedMeal = null
        this.arguments?.removeMealInfo()
    }

    override fun getLayout() = R.layout.calculator_fragment

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): CalculatorVMProviderFactory {
        return CalculatorVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }
}