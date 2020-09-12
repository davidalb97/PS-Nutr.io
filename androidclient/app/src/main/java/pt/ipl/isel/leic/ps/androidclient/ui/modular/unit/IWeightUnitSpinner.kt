package pt.ipl.isel.leic.ps.androidclient.ui.modular.unit

import android.content.Context
import android.widget.Spinner
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getWeightUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

private val log = Logger(IWeightUnitSpinner::class)

interface IWeightUnitSpinner : IUnitSpinner {

    var previousWeightUnit: WeightUnits
    var currentWeightUnit: WeightUnits

    fun onWeightUnitChange(converter: (Float) -> Float)

    fun setupWeightUnitSpinner(
        context: Context,
        weightUnitSpinner: Spinner,
        configuredUnit: WeightUnits = sharedPreferences.getWeightUnitOrDefault()
    ) {
        previousWeightUnit = configuredUnit
        currentWeightUnit = configuredUnit

        super.setupUnitSpinner(
            context = context,
            spinner = weightUnitSpinner,
            configuredUnit = configuredUnit.toString(),
            allUnits = WeightUnits.values().map(WeightUnits::toString)
        ) { selectedUnitName ->
            previousWeightUnit = currentWeightUnit
            currentWeightUnit = WeightUnits.fromValue(selectedUnitName)
            onWeightUnitChange { oldValue ->
                previousWeightUnit.convert(currentWeightUnit, oldValue)
            }
        }
    }
}