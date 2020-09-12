package pt.ipl.isel.leic.ps.androidclient.ui.fragment.create

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.IParentViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseViewModelFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRequiredTextInput
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IGlucoseUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IWeightUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.navParentViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddInsulinProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.getNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_GLUCOSE_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddInsulinProfileViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import java.util.*

class AddProfileFragment : BaseViewModelFragment<InsulinProfilesListViewModel>(),
    IWeightUnitSpinner,
    IGlucoseUnitSpinner,
    IRequiredTextInput,
    IParentViewModel {

    override val layout = R.layout.add_insulin_profile_fragment
    override val vmClass = InsulinProfilesListViewModel::class.java
    override val vMProviderFactorySupplier = ::AddInsulinProfileVMProviderFactory
    override val viewModel: InsulinProfilesListViewModel by navParentViewModel()
    private val viewModelAddProfile by lazy {
        buildViewModel(savedInstanceState, AddInsulinProfileViewModel::class.java)
    }

    override lateinit var previousWeightUnit: WeightUnits
    override lateinit var currentWeightUnit: WeightUnits
    override lateinit var previousGlucoseUnit: GlucoseUnits
    override lateinit var currentGlucoseUnit: GlucoseUnits

    private lateinit var profileNameEditText: EditText
    private lateinit var glucoseObjectiveEditText: EditText
    private lateinit var glucoseReductionEditText: EditText
    private lateinit var carbohydrateReductionEditText: EditText
    private lateinit var createButton: Button
    private lateinit var startTimeButton: Button
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeButton: Button
    private lateinit var endTimeTextView: TextView
    private lateinit var carbUnitSpinner: Spinner
    private lateinit var glucoseUnitSpinner: Spinner
    private lateinit var calendar: Calendar
    private val defaultHour by lazy { calendar.get(Calendar.HOUR_OF_DAY) }
    private val defaultMinute by lazy { calendar.get(Calendar.MINUTE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileNameEditText = view.findViewById(R.id.profile_name)
        profileNameEditText.setText(viewModelAddProfile.profileName)

        glucoseObjectiveEditText = view.findViewById(R.id.glucose_objective)
        glucoseObjectiveEditText.setText(viewModelAddProfile.glucoseObjective?.toString())

        glucoseReductionEditText = view.findViewById(R.id.glucose_amount)
        glucoseReductionEditText.setText(viewModelAddProfile.glucoseReduction?.toString())

        carbohydrateReductionEditText = view.findViewById(R.id.carbo_amount)
        carbohydrateReductionEditText.setText(viewModelAddProfile.carbReduction?.toString())

        startTimeButton = view.findViewById(R.id.start_time_label)
        startTimeTextView = view.findViewById(R.id.start_time_user)
        startTimeTextView.text = viewModelAddProfile.startTime
        startTimeButton.setOnClickListener {
            showTimePickerDialog(view, startTimeTextView, defaultHour, defaultMinute)
        }

        endTimeButton = view.findViewById(R.id.end_time_label)
        endTimeTextView = view.findViewById(R.id.end_time_user)
        endTimeTextView.text = viewModelAddProfile.endTime
        endTimeButton.setOnClickListener {
            showTimePickerDialog(view, endTimeTextView, defaultHour, defaultMinute)
        }

        val context = requireContext()

        glucoseUnitSpinner = view.findViewById(R.id.glucose_measurement_units)
        setupGlucoseUnitSpinner(context, glucoseUnitSpinner, viewModelAddProfile.currentGlucoseUnit)

        carbUnitSpinner = view.findViewById(R.id.carbohydrate_measurement_units)
        setupWeightUnitSpinner(context, carbUnitSpinner, viewModelAddProfile.currentWeightUnit)

        createButton = view.findViewById(R.id.create_custom_meal)
        calendar = Calendar.getInstance()

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
        val ctx = requireContext()

        // Checks if any field is blank
        if (!validateTextViews(ctx, profileNameEditText, startTimeTextView, endTimeTextView)) {
            return
        }

        // Checks if any field is positive
        if (!validatePositiveFloatTextViews(
                ctx,
                glucoseObjectiveEditText,
                glucoseReductionEditText,
                carbohydrateReductionEditText
            )
        ) {
            return
        }

        val convertedGlucoseObjective: Float = currentGlucoseUnit.convert(
            DEFAULT_GLUCOSE_UNIT,
            glucoseObjectiveEditText.text.toString().toFloat()
        )
        val convertedGlucoseAmount: Float = currentGlucoseUnit.convert(
            DEFAULT_GLUCOSE_UNIT,
            glucoseReductionEditText.text.toString().toFloat()
        )
        val convertedCarbAmount: Float = currentWeightUnit.convert(
            DEFAULT_WEIGHT_UNIT,
            carbohydrateReductionEditText.text.toString().toFloat()
        )

        val newProfile = InsulinProfile(
            profileName = profileNameEditText.text.toString(),
            startTime = startTimeTextView.text.toString(),
            endTime = endTimeTextView.text.toString(),
            glucoseObjective = convertedGlucoseObjective,
            glucoseAmountPerInsulin = convertedGlucoseAmount,
            carbsAmountPerInsulin = convertedCarbAmount,
            modificationDate = TimestampWithTimeZone.now()
        )

        // Checks if start time is before end time
        if (!newProfile.isValid()) {
            Toast.makeText(
                app,
                "The time period is not valid",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Asserts time period with the other profiles
        if (!newProfileOverlaps(newProfile)) {
            Toast.makeText(
                app,
                getString(R.string.This_time_period_is_already_occupied),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        viewModel.addDbInsulinProfile(newProfile, onError = {
            log.e(it)
            Toast.makeText(app, R.string.insulin_profile_add_fail, Toast.LENGTH_SHORT)
                .show()
        }) {
            viewModel.itemsChanged = true
            //Navigate to parent pop
            navigate(requireArguments().getNavigation())
        }
    }

    /**
     * Checks if it is not overlapping other insulin profiles' time periods
     * @param - The insulin profile to be evaluated
     * @param - The callback that confirms its validation
     */
    private fun newProfileOverlaps(newProfile: InsulinProfile): Boolean {
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
        viewModelAddProfile.currentWeightUnit = currentWeightUnit
        if (carbohydrateReductionEditText.text.isNotBlank()) {
            val currentValue = carbohydrateReductionEditText.text.toString().toFloat()
            carbohydrateReductionEditText.setText(converter(currentValue).toString())
        }
    }

    override fun onGlucoseUnitChange(converter: (Float) -> Float) {
        viewModelAddProfile.currentGlucoseUnit = currentGlucoseUnit
        var currentValue: Float? = glucoseObjectiveEditText.toPositiveFloatOrNull()
        if (currentValue != null) {
            glucoseObjectiveEditText.setText(
                previousGlucoseUnit.convert(currentGlucoseUnit, currentValue).toString()
            )
        }
        currentValue = glucoseReductionEditText.toPositiveFloatOrNull()
        if (currentValue != null) {
            glucoseReductionEditText.setText(
                previousGlucoseUnit.convert(currentGlucoseUnit, currentValue).toString()
            )
        }
    }

    override fun getViewModels() = super.getViewModels().plus(viewModelAddProfile)

    override fun onSaveInstanceState(outState: Bundle) {
        viewModelAddProfile.profileName = profileNameEditText.text?.toString()
        viewModelAddProfile.startTime = startTimeTextView.text?.toString()
        viewModelAddProfile.endTime = endTimeTextView.text?.toString()
        viewModelAddProfile.glucoseObjective = glucoseObjectiveEditText.toPositiveFloatOrNull()
        viewModelAddProfile.glucoseReduction = glucoseReductionEditText.toPositiveFloatOrNull()
        viewModelAddProfile.carbReduction = carbohydrateReductionEditText.toPositiveFloatOrNull()
        super.onSaveInstanceState(outState)
    }
}