package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.InsulinCalculator
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.provider.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.closeKeyboard
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealInfoViewModel
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

const val BUNDLED_MEAL_INFO_TAG = "bundledMeal"
const val CONVERTION_CONST = 18
const val MILLIGRAM_PER_DL = "mg / dL"
const val MILLIMOL_PER_L = "mmol / L"

class CalculatorFragment : Fragment() {

    lateinit var viewModelProfiles: InsulinProfilesRecyclerViewModel
    lateinit var viewModelMeal: MealInfoViewModel

    private var receivedMeal: MealInfo? = null
    private var currentProfile: InsulinProfile? = null
    private val calculator = InsulinCalculator()

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factoryProfiles =
            InsulinProfilesVMProviderFactory(savedInstanceState, rootActivity.intent)
        val factoryMealInfo =
            MealInfoVMProviderFactory(savedInstanceState, rootActivity.intent, arguments)
        viewModelProfiles = ViewModelProvider(
            rootActivity,
            factoryProfiles
        )[InsulinProfilesRecyclerViewModel::class.java]
        viewModelMeal =
            ViewModelProvider(rootActivity, factoryMealInfo)[MealInfoViewModel::class.java]

        //Read passed info from bundle
        viewModelMeal.mealInfo = arguments?.getParcelable(BUNDLE_MEAL_INFO)
        viewModelMeal.source = arguments?.getInt(BUNDLE_MEAL_SOURCE, -1)?.let {
            if (it == -1) null else Source.values()[it]
        }
        viewModelMeal.submissionId = arguments?.getInt(BUNDLE_MEAL_SUBMISSION_ID, -1)?.let {
            if (it == -1) null else it
        }
        viewModelMeal.dbId = arguments?.getLong(BUNDLE_MEAL_DB_ID, -1)?.let {
            val check: Long = -1
            if (it == check) null else it
        }

        viewModelProfiles
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.calculator_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerSetup()
        searchForBundledMeal()

        val jwt = sharedPreferences.getString(JWT, null)

        if (jwt == null) {
            Toast.makeText(
                app,
                R.string.calculator_login_warning,
                Toast.LENGTH_LONG
            ).show()
        } else {
            viewModelProfiles.jwt = jwt
        }

        viewModelProfiles.onError = {
            Toast.makeText(app, R.string.profiles_error, Toast.LENGTH_LONG)
                .show()
        }

        viewModelProfiles.update()
        observeExistingInsulinProfiles()
        setupAddMealButtonListener()
    }

    /**
     * Setups the measurement units spinner following the shared preferences.
     */
    @Suppress("UNCHECKED_CAST")
    private fun spinnerSetup() {
        val spinner = view?.findViewById<Spinner>(R.id.measurement_units)
        val spinnerAdapter: ArrayAdapter<String> = spinner!!.adapter as ArrayAdapter<String>

        val defaultUnitKey =
            sharedPreferences.getString("insulin_units", MILLIGRAM_PER_DL)

        val spinnerPosition = spinnerAdapter.getPosition(defaultUnitKey)
        spinner.setSelection(spinnerPosition)

        // Changes the blood glucose value according to the unit spinner
        val currentBloodGlucose =
            view?.findViewById<EditText>(R.id.user_blood_glucose)
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
                val value = spinner.selectedItem.toString()

                if (currentBloodGlucose!!.text.isNotBlank()) {
                    if (value == MILLIGRAM_PER_DL) {
                        currentBloodGlucose.text = convertToMG(currentBloodGlucose.text.toString())
                    } else {
                        currentBloodGlucose.text =
                            convertToMMOL(currentBloodGlucose.text.toString())
                    }
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
        val bundle: Bundle? = this.arguments
        if (bundle != null) { // || viewModel.hasMeal()
            val owner = this
            viewModelMeal.observe(owner) {
                receivedMeal = it.first()
                addBundledMealHolder()
                viewModelMeal.removeObservers(owner)
            }
            viewModelMeal.update()
        }
    }

    /**
     * Setups the CardView in the fragment that will hold the chosen meal.
     */
    private fun addBundledMealHolder() {
        val selectedMealCard =
            view?.findViewById<CardView>(R.id.calc_meal_card)
        val selectedMealName =
            view?.findViewById<TextView>(R.id.calc_meal_name)
        val selectedMealDeleteButton =
            view?.findViewById<ImageButton>(R.id.remove_meal_from_calc_button)

        selectedMealName?.text = receivedMeal?.name
        selectedMealCard?.visibility = View.VISIBLE

        selectedMealDeleteButton?.setOnClickListener {
            cleanBundledMeal(selectedMealCard)
        }
    }

    /**
     * Setups the button that allows the user to select meals.
     */
    private fun setupAddMealButtonListener() {
        val addButton =
            view?.findViewById<ImageButton>(R.id.meal_add_button)
        addButton?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.nav_add_meal_to_calculator)
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
        getActualProfile { profile ->
            currentProfile = profile
            showActualProfileDetails(profile!!)
        }
    }

    /**
     * Searches for a profile that matches the current time
     */
    private fun getActualProfile(cb: (InsulinProfile?) -> Unit) {

        // Gets actual time in hh:mm format
        val time = Calendar.getInstance().time

        val profiles = viewModelProfiles.items
        if (profiles.isEmpty()) {
            cb(null)
            showNoExistingProfilesToast()
            return
        }
        val timeInstance = SimpleDateFormat("HH:MM")
        viewModelProfiles.items.forEach { savedProfile ->
            val parsedSavedStartTime = timeInstance.parse(savedProfile.startTime)
            val parsedSavedEndTime = timeInstance.parse(savedProfile.endTime)
            if (time.after(parsedSavedStartTime) && time.before(parsedSavedEndTime)) {
                cb(savedProfile) // TODO: callback is not affected, see condition
            }
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
                if (spinner?.selectedItem.toString() == MILLIMOL_PER_L) {
                    bloodGlucoseValue =
                        convertToMG(currentBloodGlucose.text.toString())
                            .toString().toDouble().toInt()
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
     * Converts blood glucose value from mg / dL to mmol / L.
     */
    private fun convertToMMOL(currentValue: String): Editable? =
        SpannableStringBuilder(
            (currentValue.toDouble() * CONVERTION_CONST)
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_UP)
                .toFloat()
                .toString()
        )

    /**
     * Converts blood glucose value from mmol / L to mg / dL.
     */
    private fun convertToMG(currentValue: String): Editable? =
        SpannableStringBuilder(
            (currentValue.toDouble() / CONVERTION_CONST)
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_UP)
                .toFloat()
                .toString()
        )

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
        this.arguments?.remove(BUNDLED_MEAL_INFO_TAG)
    }
}