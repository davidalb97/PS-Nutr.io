package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import pt.ipl.isel.leic.ps.androidclient.R

// TODO: remove if only used once
interface IChart {

    val chartId: Int
    var chart: BarChart

    fun setupChart(view: View, values: Collection<BarEntry>) {
        chart = view.findViewById(R.id.portion_chart)
        if (values.isEmpty()) {
            chart.setNoDataText("There are no portions available. Be the first to add one!")
        } else {
            chart.axisLeft.granularity = 1f
            chart.axisRight.setDrawLabels(false)

            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.setDrawGridLines(false)
            chart.xAxis.setDrawGridLines(false)
            chart.animateY(1000);

            val dataSets = arrayListOf<IBarDataSet>()
            val graphDataSet = BarDataSet(values.toList(), "Portions")
            dataSets.add(graphDataSet)
            val lineData = BarData(dataSets)
            chart.data = lineData
        }
        chart.invalidate()
    }
}