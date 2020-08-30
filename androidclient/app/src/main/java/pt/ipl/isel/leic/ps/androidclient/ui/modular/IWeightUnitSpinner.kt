package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.getWeightUnitOrDefault
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

interface IWeightUnitSpinner {

    var previousUnit: WeightUnits
    var currentUnit: WeightUnits

    fun onWeightUnitChange(converter: (Float) -> Float)

    fun setupWeightUnitSpinner(context: Context, weightUnitSpinner: Spinner) {
        val log = Logger(IWeightUnitSpinner::class)
        val spinnerAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            mutableListOf<String>()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightUnitSpinner.adapter = spinnerAdapter

        //Add all units
        spinnerAdapter.addAll(WeightUnits.values().map { it.toString() })

        //Get user configured unit
        val configuredUnit = sharedPreferences.getWeightUnitOrDefault()
        currentUnit =  WeightUnits.fromValue(configuredUnit)
        previousUnit = currentUnit

        //Set selected unit to configured unit
        val spinnerPosition = spinnerAdapter.getPosition(configuredUnit)
        weightUnitSpinner.setSelection(spinnerPosition)

        weightUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) =
                throw NotImplementedError("This function is not implemented")

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                previousUnit = currentUnit
                currentUnit = WeightUnits.fromValue(weightUnitSpinner.selectedItem.toString())

                log.v("Changing unit from $previousUnit to $currentUnit")

                //Notify listeners of unit change passing the converted function
                onWeightUnitChange { oldValue ->
                    previousUnit.convert(currentUnit, oldValue)
                }
            }
        }
    }
}