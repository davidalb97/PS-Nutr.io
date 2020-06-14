
package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity

private const val NO_FILTER = " "

class SpinnerHandler(
    activity: FragmentActivity,
    @IdRes private val spinnerId: Int
) {
    private val log = Logger("FilterSpinner:$spinnerId")
    private val arrayAdapter = initArrayAdapter(activity)
    private val spinner = initSpinner(activity)
    private var itemClickListener: ((String) -> Unit)? = null

    private fun initArrayAdapter(activity: FragmentActivity): ArrayAdapter<String> {
        val arrayAdapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            mutableListOf<String>()
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return arrayAdapter
    }

    private fun initSpinner(
        activity: FragmentActivity
    ): Spinner {

        val spinner: Spinner = activity.findViewById(spinnerId)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String
                log.logv("Selected: \"$selectedItem\"")

                if(selectedItem != NO_FILTER) {
                    log.logv("Selected filterId: \"$selectedItem\"")

                    arrayAdapter.remove(selectedItem)
                    spinner.setSelection(0)            //Forces first position to allow re-click after removal
                    if (itemClickListener != null) {
                        itemClickListener!!(selectedItem)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        return spinner
    }

    fun addAll(items: ArrayList<String>) {
        arrayAdapter.addAll(items)
    }

    fun addFilterData(item: String) {
        log.logv("Removed \"$item\" from Spinner's RecyclerView!")
        arrayAdapter.setNotifyOnChange(false)
        arrayAdapter.add(item)
        arrayAdapter.sort(String::compareTo)
        arrayAdapter.setNotifyOnChange(true)
        arrayAdapter.notifyDataSetChanged()
    }

    fun removeAllFilterData(item: Iterable<String>) {
        arrayAdapter.setNotifyOnChange(false)
        item.forEach {
            arrayAdapter.remove(it)
            log.logv("Removed \"$item\" from Spinner!")
        }
        arrayAdapter.setNotifyOnChange(true)
        arrayAdapter.notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        itemClickListener = listener
    }
}