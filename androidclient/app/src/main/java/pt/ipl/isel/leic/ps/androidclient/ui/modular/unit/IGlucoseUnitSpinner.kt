package pt.ipl.isel.leic.ps.androidclient.ui.modular.unit

import android.content.Context
import android.view.View
import android.widget.Spinner
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getGlucoseUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits

private val log = Logger(IGlucoseUnitSpinner::class)

interface IGlucoseUnitSpinner : IUnitSpinner {

    val glucoseUnitSpinnerId: Int
    var glucoseUnitSpinner: Spinner
    var previousGlucoseUnit: GlucoseUnits
    var currentGlucoseUnit: GlucoseUnits

    fun onGlucoseUnitChange(converter: (Float) -> Float)

    fun setupGlucoseUnitSpinner(
        view: View,
        context: Context,
        configuredUnit: GlucoseUnits = sharedPreferences.getGlucoseUnitOrDefault()
    ) {
        currentGlucoseUnit = configuredUnit
        previousGlucoseUnit = configuredUnit

        glucoseUnitSpinner = super.setupUnitSpinner(
            view = view,
            context = context,
            spinnerId = glucoseUnitSpinnerId,
            configuredUnit = configuredUnit.toString(),
            allUnits = GlucoseUnits.values().map(GlucoseUnits::toString)
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

    fun setGlucoseUnitSpinnerSelection(unit: GlucoseUnits) {
        super.setUnitSpinnerSelection(glucoseUnitSpinner, unit.toString())
    }
}