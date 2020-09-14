package pt.ipl.isel.leic.ps.androidclient.ui.modular.unit

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.IdRes

interface IUnitSpinner {

    fun setupUnitSpinner(
        view: View,
        context: Context,
        @IdRes spinnerId: Int,
        configuredUnit: String,
        allUnits: Collection<String>,
        onUnitChange: (String) -> Unit
    ): Spinner {
        val spinner: Spinner = view.findViewById(spinnerId)
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
            ) = onUnitChange(spinner.selectedItem.toString())
        }
        return spinner
    }

    fun setUnitSpinnerSelection(spinner: Spinner, unitName: String) {
        val adapter = spinner.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(unitName)
        spinner.setSelection(position)
    }
}