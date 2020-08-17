package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import java.text.SimpleDateFormat
import java.util.*

class AddProfileFragment : BaseFragment() {

    private lateinit var viewModel: InsulinProfilesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, InsulinProfilesListViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileName = view.findViewById<EditText>(R.id.profile_name)
        var glucoseObjective = view.findViewById<EditText>(R.id.glucose_objective)
        var glucoseAmount = view.findViewById<EditText>(R.id.glucose_amount)
        var carboAmount = view.findViewById<EditText>(R.id.carbo_amount)
        val createButton = view.findViewById<Button>(R.id.create_custom_meal)
        val addStartTime = view.findViewById<Button>(R.id.start_time_label)
        val startTimeUser = view.findViewById<TextView>(R.id.start_time_user)
        val addEndTime = view.findViewById<Button>(R.id.end_time_label)
        val endTimeUser = view.findViewById<TextView>(R.id.end_time_user)

        val calendar = Calendar.getInstance()
        val defaultHour = calendar.get(Calendar.HOUR_OF_DAY)
        val defaultMinute = calendar.get(Calendar.MINUTE)

        addStartTime.setOnClickListener {
            setupTimePickerDialog(startTimeUser, defaultHour, defaultMinute)
                .show()
        }

        addEndTime.setOnClickListener {
            setupTimePickerDialog(endTimeUser, defaultHour, defaultMinute)
                .show()
        }

        setupGlucoseSpinner()
        setupCarbohydrateSpinner()

        // Get all profiles before the new insertion
        viewModel.update()

        createButton.setOnClickListener {
            // Checks if any field is blank
            val anyFieldBlank =
                listOf(
                    profileName,
                    startTimeUser,
                    endTimeUser,
                    glucoseObjective,
                    glucoseAmount,
                    carboAmount
                ).any { it.text.isBlank() }

            if (anyFieldBlank) {
                Toast.makeText(
                    app,
                    getString(R.string.Fill_in_all_the_available_fields),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            /**
             * If the user used a non-standard unit, convert back to standard in order to store
             * Ex: mmol / L -> mg / dL ////// ounces -> grams
             */
            val glucoseUnitSpinner = view.findViewById<Spinner>(R.id.glucose_measurement_units)
            val carbUnitSpinner = view.findViewById<Spinner>(R.id.carbohydrate_measurement_units)

            var convertedGlucoseObjective: Float? = null
            var convertedGlucoseAmount: Float? = null
            var convertedCarbAmount: Float? = null

            val selectedGlucoseUnit =
                GlucoseUnits.fromValue(glucoseUnitSpinner.selectedItem.toString())
            val selectedCarbohydratesUnit =
                WeightUnits.fromValue(carbUnitSpinner.selectedItem.toString())

            if (selectedGlucoseUnit != DEFAULT_GLUCOSE_UNIT) {
                convertedGlucoseObjective = selectedGlucoseUnit
                    .convert(DEFAULT_GLUCOSE_UNIT, glucoseObjective.text.toString().toFloat())
                convertedGlucoseAmount = selectedGlucoseUnit
                    .convert(DEFAULT_GLUCOSE_UNIT, glucoseAmount.text.toString().toFloat())
            }

            if (selectedCarbohydratesUnit != DEFAULT_WEIGHT_UNIT) {
                convertedCarbAmount = selectedCarbohydratesUnit
                    .convert(DEFAULT_WEIGHT_UNIT, carboAmount.text.toString().toFloat())
            }

            // Creates profile if everything is ok until here
            val profile = InsulinProfile(
                profileName.text.toString(),
                startTimeUser.text.toString(),
                endTimeUser.text.toString(),
                convertedGlucoseObjective ?: glucoseObjective.text.toString().toFloat(),
                convertedGlucoseAmount ?: glucoseAmount.text.toString().toFloat(),
                convertedCarbAmount ?: carboAmount.text.toString().toFloat(),
                TimestampWithTimeZone.now()
            )

            // Asserts time period with the other profiles
            profileTimesValidation(profile) { isValid ->
                if (!isValid) {
                    Toast.makeText(
                        app,
                        getString(R.string.This_time_period_is_already_occupied),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel
                        .addDbInsulinProfile(profile, getUserSession())
                        .setOnPostExecute { parentFragmentManager.popBackStack() }
                        .execute()
                }
            }
        }
    }

    /**
     * Setups the glucose spinner to allow the user to choose different measurement units
     */
    private fun setupGlucoseSpinner() {
        val spinner = view?.findViewById<Spinner>(R.id.glucose_measurement_units)
        val spinnerAdapter: ArrayAdapter<String> = spinner!!.adapter as ArrayAdapter<String>

        val configuredUnit = NutrioApp.sharedPreferences.getGlucoseUnitOrDefault()

        val spinnerPosition = spinnerAdapter.getPosition(configuredUnit)
        spinner.setSelection(spinnerPosition)

        // Changes the blood glucose value according to the unit spinner
        val glucoseObjective = view?.findViewById<EditText>(R.id.glucose_objective)
        val glucoseAmount = view?.findViewById<EditText>(R.id.glucose_amount)

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
                if (glucoseObjective!!.text.isNotBlank()) {
                    convertGlucoseValue(spinner, glucoseObjective)
                }

                if (glucoseAmount!!.text.isNotBlank()) {
                    convertGlucoseValue(spinner, glucoseAmount)
                }
            }
        }
    }

    private fun setupCarbohydrateSpinner() {
        val spinner = view?.findViewById<Spinner>(R.id.carbohydrate_measurement_units)
        val spinnerAdapter: ArrayAdapter<String> = spinner!!.adapter as ArrayAdapter<String>

        val configuredUnit = NutrioApp.sharedPreferences.getWeightUnitOrDefault()

        val spinnerPosition = spinnerAdapter.getPosition(configuredUnit)
        spinner.setSelection(spinnerPosition)

        // Changes the carbohydrate amount value according to the unit spinner
        val carbohydrateAmount = view?.findViewById<EditText>(R.id.carbo_amount)

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
                if (carbohydrateAmount!!.text.isNotBlank()) {
                    convertCarbValue(spinner, carbohydrateAmount)
                }
            }
        }
    }

    /**
     * Converts the passed glucose unit, based on the spinner's selected options
     * @param spinner - The spinner where the selection happens;
     * @param textBox - The EditText box which holds the value to be converted.
     */
    private fun convertGlucoseValue(spinner: Spinner, textBox: EditText) {
        val newUnit = GlucoseUnits.fromValue(spinner.selectedItem.toString())
        val oldUnit = when (newUnit) {
            GlucoseUnits.MILLI_GRAM_PER_DL -> GlucoseUnits.MILLI_MOL_PER_L
            GlucoseUnits.MILLI_MOL_PER_L -> GlucoseUnits.MILLI_GRAM_PER_DL
        }
        val currentValue = textBox.text.toString().toFloat()
        textBox.setText(oldUnit.convert(newUnit, currentValue).toString())
    }

    /**
     * Converts the passed carbohydrate unit, based on the spinner's selected options
     * @param spinner - The spinner where the selection happens;
     * @param textBox - The EditText box which holds the value to be converted.
     */
    private fun convertCarbValue(spinner: Spinner, textBox: EditText) {
        val newUnit = WeightUnits.fromValue(spinner.selectedItem.toString())
        val oldUnit = when (newUnit) {
            WeightUnits.GRAMS -> WeightUnits.OUNCES
            WeightUnits.OUNCES -> WeightUnits.GRAMS
        }
        val currentValue = textBox.text.toString().toFloat()
        textBox.setText(oldUnit.convert(newUnit, currentValue).toString())
    }

    /**
     * Checks if the time period passed to the time pickers is valid
     * @param - The insulin profile to be evaluated
     * @param - The callback that confirms its validation
     */
    private fun profileTimesValidation(
        profileDb: InsulinProfile,
        cb: (Boolean) -> Unit
    ) {
        val timeInstance = SimpleDateFormat("HH:MM")

        val parsedStartTime = timeInstance.parse(profileDb.startTime)!!
        val parsedEndTime = timeInstance.parse(profileDb.endTime)!!

        // Checks if start time is before end time
        if (parsedEndTime.before(parsedStartTime)) {
            Toast.makeText(
                app,
                "The time period is not valid",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Checks if it is not overlapping other time periods
        val insulinProfiles = viewModel.items
        var isValid = true
        if (insulinProfiles.isNotEmpty()) {
            insulinProfiles.forEach { savedProfile ->
                val parsedSavedStartTime = timeInstance.parse(savedProfile.startTime)!!
                val parsedSavedEndTime = timeInstance.parse(savedProfile.endTime)!!

                if (!(parsedEndTime.before(parsedSavedStartTime) ||
                            parsedStartTime.after(parsedSavedEndTime))
                ) {
                    isValid = false
                }
            }
        }
        cb(isValid)
    }

    /**
     * Setups each time picker dialog saving the chosen values to a TextView
     * so it can also display to the user
     * @param textView The [TextView] to set
     * @param defaultHour The default time (hours)
     * @param defaultMinute The default time (minutes)
     */
    private fun setupTimePickerDialog(
        textView: TextView,
        defaultHour: Int,
        defaultMinute: Int
    ): TimePickerDialog {
        return TimePickerDialog(
            view?.context,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
                textView.text = String.format("%02d:%02d", hour, minute)
            },
            defaultHour,
            defaultMinute,
            true
        )
    }

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): InsulinProfilesVMProviderFactory {
        return InsulinProfilesVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getLayout() = R.layout.add_insulin_profile_fragment
}