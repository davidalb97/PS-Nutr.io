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

private val log = Logger(IGlucoseUnitSpinner::class)

interface IGlucoseUnitSpinner {

    var previousGlucoseUnit: GlucoseUnits
    var currentGlucoseUnit: GlucoseUnits

    fun onGlucoseUnitChange(converter: (Float) -> Float)

    fun setupGlucoseUnitSpinner(context: Context, glucoseUnitSpinner: Spinner) {
        val spinnerAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            mutableListOf<String>()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        glucoseUnitSpinner.adapter = spinnerAdapter

        //Add all units
        spinnerAdapter.addAll(GlucoseUnits.values().map { it.toString() })

        //Get user configured unit
        val configuredUnit = sharedPreferences.getGlucoseUnitOrDefault()
        currentGlucoseUnit = GlucoseUnits.fromValue(configuredUnit)
        previousGlucoseUnit = currentGlucoseUnit

        //Set selected unit to configured unit
        val spinnerPosition = spinnerAdapter.getPosition(configuredUnit)
        glucoseUnitSpinner.setSelection(spinnerPosition)

        glucoseUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) =
                throw NotImplementedError("This function is not implemented")

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                previousGlucoseUnit = currentGlucoseUnit
                currentGlucoseUnit =
                    GlucoseUnits.fromValue(glucoseUnitSpinner.selectedItem.toString())

                log.v("Changing unit from $previousGlucoseUnit to $currentGlucoseUnit")

                //Notify listeners of unit change passing the converted function
                onGlucoseUnitChange { oldValue ->
                    previousGlucoseUnit.convert(currentGlucoseUnit, oldValue)
                }
            }
        }
    }
}