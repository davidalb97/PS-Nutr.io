package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRequiredTextInput
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IGlucoseUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IWeightUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_GLUCOSE_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import java.text.SimpleDateFormat
import java.util.*

class AddProfileFragment : BaseFragment(),
    IWeightUnitSpinner,
    IGlucoseUnitSpinner,
    IRequiredTextInput
{


    override lateinit var previousWeightUnit: WeightUnits
    override lateinit var currentWeightUnit: WeightUnits
    override lateinit var previousGlucoseUnit: GlucoseUnits
    override lateinit var currentGlucoseUnit: GlucoseUnits

    private lateinit var viewModel: InsulinProfilesListViewModel
    private lateinit var profileName: EditText
    private lateinit var glucoseObjective: EditText
    private lateinit var glucoseAmount: EditText
    private lateinit var carbohydrateAmount: EditText
    private lateinit var createButton: Button
    private lateinit var addStartTime: Button
    private lateinit var startTimeUser: TextView
    private lateinit var addEndTime: Button
    private lateinit var endTimeUser: TextView
    private lateinit var carbUnitSpinner: Spinner
    private lateinit var glucoseUnitSpinner: Spinner
    private lateinit var calendar: Calendar
    private val defaultHour by lazy { calendar.get(Calendar.HOUR_OF_DAY) }
    private val defaultMinute by lazy { calendar.get(Calendar.MINUTE) }

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

        profileName = view.findViewById(R.id.profile_name)
        glucoseObjective = view.findViewById(R.id.glucose_objective)
        glucoseAmount = view.findViewById(R.id.glucose_amount)
        carbohydrateAmount = view.findViewById(R.id.carbo_amount)
        createButton = view.findViewById(R.id.create_custom_meal)
        addStartTime = view.findViewById(R.id.start_time_label)
        startTimeUser = view.findViewById(R.id.start_time_user)
        addEndTime = view.findViewById(R.id.end_time_label)
        endTimeUser = view.findViewById(R.id.end_time_user)
        carbUnitSpinner = view.findViewById(R.id.carbohydrate_measurement_units)
        glucoseUnitSpinner = view.findViewById(R.id.glucose_measurement_units)

        calendar = Calendar.getInstance()

        addStartTime.setOnClickListener {
            showTimePickerDialog(view, startTimeUser, defaultHour, defaultMinute)
        }
        addEndTime.setOnClickListener {
            showTimePickerDialog(view, endTimeUser, defaultHour, defaultMinute)
        }

        setupGlucoseUnitSpinner(requireContext(), glucoseUnitSpinner)
        setupWeightUnitSpinner(requireContext(), carbUnitSpinner)

        // Get all profiles before the new insertion
        viewModel.update()

        createButton.setOnClickListener {
            onCreateProfile()
        }
    }

    private fun onCreateProfile() {

        // Checks if any field is blank
        if (!validateTextViews(
                requireContext(),
                profileName,
                startTimeUser,
                endTimeUser,
                glucoseObjective,
                glucoseAmount,
                carbohydrateAmount
            )
        ) {
            return
        }

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
                .convert(DEFAULT_WEIGHT_UNIT, carbohydrateAmount.text.toString().toFloat())
        }

        // Creates profile if everything is ok until here
        val profile = InsulinProfile(
            profileName.text.toString(),
            startTimeUser.text.toString(),
            endTimeUser.text.toString(),
            convertedGlucoseObjective ?: glucoseObjective.text.toString().toFloat(),
            convertedGlucoseAmount ?: glucoseAmount.text.toString().toFloat(),
            convertedCarbAmount ?: carbohydrateAmount.text.toString().toFloat(),
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
    private fun showTimePickerDialog(
        view: View,
        textView: TextView,
        defaultHour: Int,
        defaultMinute: Int
    ) {
        TimePickerDialog(
            view.context,
            { _: TimePicker, hour: Int, minute: Int ->
                textView.text = String.format("%02d:%02d", hour, minute)
            },
            defaultHour,
            defaultMinute,
            true
        ).show()
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

    /**
     * Converts the passed carbohydrate unit, based on the spinner's selected options
     */
    override fun onWeightUnitChange(converter: (Float) -> Float) {
        if (carbohydrateAmount.text.isNotBlank()) {
            val currentValue = carbohydrateAmount.text.toString().toFloat()
            carbohydrateAmount.setText(converter(currentValue).toString())
        }
    }

    override fun onGlucoseUnitChange(converter: (Float) -> Float) {
        if (glucoseObjective.text.isNotBlank()) {
            convertGlucoseValue(glucoseUnitSpinner, glucoseObjective)
        }

        if (glucoseAmount.text.isNotBlank()) {
            convertGlucoseValue(glucoseUnitSpinner, glucoseAmount)
        }
    }
}