package pt.ipl.isel.leic.ps.androidclient.ui.modular.unit

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getWeightUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

private val log = Logger(IWeightUnitSpinner::class)

interface IWeightUnitSpinner : IUnitSpinner {

    val weightUnitSpinnerId: Int
    var weightUnitSpinner: Spinner
    var previousWeightUnit: WeightUnits
    var currentWeightUnit: WeightUnits

    fun onWeightUnitChange(converter: (Float) -> Float)

    fun setupWeightUnitSpinner(
        view: View,
        context: Context,
        configuredUnit: WeightUnits = sharedPreferences.getWeightUnitOrDefault()
    ) {
        previousWeightUnit = configuredUnit
        currentWeightUnit = configuredUnit

        weightUnitSpinner = super.setupUnitSpinner(
            view = view,
            context = context,
            spinnerId = weightUnitSpinnerId,
            configuredUnit = configuredUnit.toString(),
            allUnits = WeightUnits.values().map(WeightUnits::toString)
        ) { selectedUnitName ->
            previousWeightUnit = currentWeightUnit
            currentWeightUnit = WeightUnits.fromValue(selectedUnitName)

            log.v("Changing unit from $previousWeightUnit to $currentWeightUnit")

            //Notify listeners of unit change passing the converted function
            onWeightUnitChange { oldValue ->
                previousWeightUnit.convert(currentWeightUnit, oldValue)
            }
        }
    }

    fun setWeightUnitSpinnerSelection(unit: WeightUnits) {
        super.setUnitSpinnerSelection(weightUnitSpinner, unit.toString())
    }
}