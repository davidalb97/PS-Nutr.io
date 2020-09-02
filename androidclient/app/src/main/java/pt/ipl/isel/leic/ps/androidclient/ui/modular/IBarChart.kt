package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import pt.ipl.isel.leic.ps.androidclient.R

interface IBarChart {

    val chartId: Int
    var chart: BarChart
    var noDataText: String?

    fun setupChart(view: View, values: Collection<BarEntry>) {
        chart = view.findViewById(R.id.portion_chart)
        if (values.isEmpty()) {
            chart.setNoDataText(noDataText)
        } else {
            setupChartSettings(values)
            setupChartData(values)
        }
        refreshChart()
    }

    fun setupChartSettings(values: Collection<BarEntry>)

    fun setupChartData(values: Collection<BarEntry>)

    fun BarChart.setXAxisGranularity(value: Float): BarChart {
        chart.xAxis.granularity = value
        return chart
    }

    fun BarChart.setYAxisGranularity(value: Float): BarChart {
        chart.axisLeft.granularity = value
        chart.axisRight.granularity = value
        return chart
    }

    fun BarChart.setXAxisMin(value: Float): BarChart {
        chart.xAxis.axisMinimum = value
        return chart
    }

    fun BarChart.setXAxisMax(value: Float): BarChart {
        chart.xAxis.axisMaximum = value
        return chart
    }

    fun BarChart.setXDrawGridLines(value: Boolean): BarChart {
        chart.xAxis.setDrawGridLines(value)
        return chart
    }

    fun BarChart.setYDrawGridLines(value: Boolean): BarChart {
        chart.axisLeft.setDrawGridLines(value)
        chart.axisRight.setDrawGridLines(value)
        return chart
    }

    fun BarChart.setChartDescription(value: Boolean): BarChart {
        chart.description.isEnabled = false;
        return chart
    }

    fun BarChart.setXAxisPosition(value: XAxis.XAxisPosition): BarChart {
        chart.xAxis.position = value
        return chart
    }

    fun BarChart.setYAnimationTime(time: Int): BarChart {
        chart.animateY(time)
        return chart
    }

    // Always finish the composition with this method
    fun BarChart.setupBarData(barData: BarData) {
        chart.data = barData
    }

    fun refreshChart() {
        chart.notifyDataSetChanged()
        chart.invalidate()
    }
}