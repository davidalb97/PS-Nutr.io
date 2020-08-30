package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import pt.ipl.isel.leic.ps.androidclient.R

// TODO: remove if only used once
interface IChart {

    val chartId: Int
    var chart: LineChart

    fun setupChart(view: View, values: Collection<Entry>) {
        chart = view.findViewById(R.id.portion_chart)
        if (values.isEmpty()) {
            chart.setNoDataText("There are no portions available. Be the first and add one!")
        } else {
            chart.axisLeft.granularity = 1f
            chart.axisRight.granularity = 1f
            val dataSets = arrayListOf<ILineDataSet>()
            val graphDataSet = LineDataSet(values.toList(), "Portions")
            dataSets.add(graphDataSet)
            val lineData = LineData(dataSets)
            chart.data = lineData
        }
        chart.invalidate()
    }
}