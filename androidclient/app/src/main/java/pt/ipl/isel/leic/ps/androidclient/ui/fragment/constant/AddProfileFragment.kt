package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseViewModelFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRequiredTextInput
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IGlucoseUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IWeightUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_GLUCOSE_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import java.text.SimpleDateFormat
import java.util.*

class AddProfileFragment : BaseViewModelFragment<InsulinProfilesListViewModel>(),
    IWeightUnitSpinner,
    IGlucoseUnitSpinner,
    IRequiredTextInput {

    override val layout = R.layout.add_insulin_profile_fragment
    override val vmClass = InsulinProfilesListViewModel::class.java
    override val vMProviderFactorySupplier = ::InsulinProfilesVMProviderFactory

    override lateinit var previousWeightUnit: WeightUnits
    override lateinit var currentWeightUnit: WeightUnits
    override lateinit var previousGlucoseUnit: GlucoseUnits
    override lateinit var currentGlucoseUnit: GlucoseUnits

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
        viewModel.observe(this) {
            viewModel.removeObservers(this)
            createButton.setOnClickListener {
                onCreateProfile()
            }
        }
        viewModel.setupList()
        createButton.setOnClickListener {
            Toast.makeText(app, R.string.loading_profiles, Toast.LENGTH_SHORT).show()
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

        val convertedGlucoseObjective: Float = currentGlucoseUnit.convert(
            DEFAULT_GLUCOSE_UNIT,
            glucoseObjective.text.toString().toFloat()
        )
        val convertedGlucoseAmount: Float = currentGlucoseUnit.convert(
            DEFAULT_GLUCOSE_UNIT,
            glucoseAmount.text.toString().toFloat()
        )
        val convertedCarbAmount: Float = currentWeightUnit.convert(
            DEFAULT_WEIGHT_UNIT,
            carbohydrateAmount.text.toString().toFloat()
        )

        val profile = InsulinProfile(
            profileName = profileName.text.toString(),
            startTime = startTimeUser.text.toString(),
            endTime = endTimeUser.text.toString(),
            glucoseObjective = convertedGlucoseObjective,
            glucoseAmountPerInsulin = convertedGlucoseAmount,
            carbsAmountPerInsulin = convertedCarbAmount,
            modificationDate = TimestampWithTimeZone.now()
        )

        // Asserts time period with the other profiles
        if (!isProfileValid(profile)) {
            Toast.makeText(
                app,
                getString(R.string.This_time_period_is_already_occupied),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        viewModel.addDbInsulinProfile(profile, onError = {
            log.e(it)
            Toast.makeText(app, R.string.insulin_profile_add_fail, Toast.LENGTH_SHORT)
                .show()
        }) {
            parentFragmentManager.popBackStack()
        }
    }

    /**
     * Checks if the time period passed to the time pickers is valid
     * @param - The insulin profile to be evaluated
     * @param - The callback that confirms its validation
     */
    private fun isProfileValid(newProfile: InsulinProfile): Boolean {

        // Checks if start time is before end time
        if (!newProfile.isValid()) {
            Toast.makeText(
                app,
                "The time period is not valid",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        // Checks if it is not overlapping other time periods
        return viewModel.items.none(newProfile::overlaps)
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
            val currentValue = glucoseObjective.text.toString().toFloat()
            glucoseObjective.setText(
                previousGlucoseUnit.convert(currentGlucoseUnit, currentValue).toString()
            )
        }

        if (glucoseAmount.text.isNotBlank()) {
            val currentValue = glucoseAmount.text.toString().toFloat()
            glucoseObjective.setText(
                previousGlucoseUnit.convert(currentGlucoseUnit, currentValue).toString()
            )
        }
    }
}