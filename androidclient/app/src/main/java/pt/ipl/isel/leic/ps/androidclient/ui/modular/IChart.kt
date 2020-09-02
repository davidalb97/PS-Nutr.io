package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R

interface IChart {

    val chartId: Int
    var chart: BarChart

    fun setupChart(view: View, values: Collection<BarEntry>) {
        chart = view.findViewById(R.id.portion_chart)
        if (values.isEmpty()) {
            chart.setNoDataText(app.getString(R.string.no_portions_chart_message))
        } else {

            val xAxis = chart.xAxis
            val yAxisLeft = chart.axisLeft
            val yAxisRight = chart.axisRight

            yAxisLeft.granularity = 1f
            xAxis.granularity = 10f
            xAxis.axisMinimum =  0f
            xAxis.axisMaximum = values.maxOf { it.x } + xAxis.granularity
            chart.description.isEnabled = false;

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            yAxisRight.setDrawLabels(false)
            yAxisRight.setDrawGridLines(false)
            yAxisLeft.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
            chart.animateY(1000);

            val dataSets = arrayListOf<IBarDataSet>()
            val graphDataSet = BarDataSet(values.toList(), "Portions")
            dataSets.add(graphDataSet)
            val lineData = BarData(dataSets)
            chart.data = lineData
        }
        invalidateChart()
    }

    fun invalidateChart() = chart.invalidate()
}