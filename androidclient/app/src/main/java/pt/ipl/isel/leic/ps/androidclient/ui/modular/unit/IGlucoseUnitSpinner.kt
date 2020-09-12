package pt.ipl.isel.leic.ps.androidclient.ui.modular.unit

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getGlucoseUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

private val log = Logger(IGlucoseUnitSpinner::class)

interface IGlucoseUnitSpinner: IUnitSpinner {

    var previousGlucoseUnit: GlucoseUnits
    var currentGlucoseUnit: GlucoseUnits

    fun onGlucoseUnitChange(converter: (Float) -> Float)

    fun setupGlucoseUnitSpinner(
        context: Context,
        glucoseUnitSpinner: Spinner,
        configuredUnit: GlucoseUnits = sharedPreferences.getGlucoseUnitOrDefault()
    ) {
        currentGlucoseUnit = configuredUnit
        previousGlucoseUnit = configuredUnit

        super.setupUnitSpinner(
            context = context,
            spinner = glucoseUnitSpinner,
            configuredUnit = configuredUnit.toString(),
            allUnits = WeightUnits.values().map(WeightUnits::toString)
        ) { selectedUnitName ->
            previousGlucoseUnit = currentGlucoseUnit
            currentGlucoseUnit = GlucoseUnits.fromValue(selectedUnitName)

            log.v("Changing unit from $previousGlucoseUnit to $currentGlucoseUnit")

            //Notify listeners of unit change passing the converted function
            onGlucoseUnitChange { oldValue ->
                previousGlucoseUnit.convert(currentGlucoseUnit, oldValue)
            }
        }
    }
}