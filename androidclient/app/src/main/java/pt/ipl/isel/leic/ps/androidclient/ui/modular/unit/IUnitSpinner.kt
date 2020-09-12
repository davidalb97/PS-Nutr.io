package pt.ipl.isel.leic.ps.androidclient.ui.modular.unit

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

interface IUnitSpinner {

    fun setupUnitSpinner(
        context: Context,
        spinner: Spinner,
        configuredUnit: String,
        allUnits: Collection<String>,
        onUnitChange: (String) -> Unit
    ) {
        val spinnerAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            mutableListOf<String>()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        //Add all units
        spinnerAdapter.addAll(allUnits)

        //Set selected unit to configured unit
        val spinnerPosition = spinnerAdapter.getPosition(configuredUnit)
        spinner.setSelection(spinnerPosition)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) =
                throw NotImplementedError("This function is not implemented")

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onUnitChange(spinner.selectedItem.toString())
            }
        }
    }
}